package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.service.ActivityLogService;
import com.bupt.tarecruitment.util.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Servlet that handles CV file uploads and deletions for applicants.
 *
 * <p>Supports two operations via HTTP POST, distinguished by the {@code action}
 * request parameter:</p>
 * <ul>
 *   <li>{@code upload} – validates, stores, and records a new CV file</li>
 *   <li>{@code delete} – removes the existing CV file from disk and clears the
 *       metadata on the applicant record</li>
 * </ul>
 *
 * <p>Physical files are stored in {@code WEB-INF/data/cvs/} using the applicant's
 * ID as the base filename (e.g. {@code u001.pdf}). Only PDF and Word documents
 * up to {@value #MAX_FILE_SIZE} bytes are accepted.</p>
 *
 * <p>Access is restricted to users with the {@code APPLICANT} role. Each applicant
 * can only modify their own CV.</p>
 *
 * <p>This servlet is annotated with {@link MultipartConfig} to enable
 * {@code multipart/form-data} processing by the container without requiring
 * a {@code <multipart-config>} block in {@code web.xml}.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     CvDownloadServlet
 * @see     ActivityLogService#UPLOAD_CV
 * @see     ActivityLogService#DELETE_CV
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1 MB – buffer to disk after this
        maxFileSize       = 2 * 1024 * 1024,  // 2 MB – per-file limit
        maxRequestSize    = 3 * 1024 * 1024   // 3 MB – total request limit
)
public class CvUploadServlet extends BaseServlet {

    /** Maximum permitted file size in bytes (2 MB). */
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024L;

    /** Allowed file extensions (lower-case, without the leading dot). */
    private static final Set<String> ALLOWED_EXTENSIONS =
            new HashSet<>(Arrays.asList("pdf", "doc", "docx"));

    /** Allowed MIME types corresponding to {@link #ALLOWED_EXTENSIONS}. */
    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(Arrays.asList(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    ));

    /** Sub-directory name inside the data directory where CV files are stored. */
    private static final String CV_DIR = "cvs";

    // -------------------------------------------------------------------------
    // HTTP handlers
    // -------------------------------------------------------------------------

    /**
     * Handles CV upload and delete POST requests.
     *
     * @param request  the {@link HttpServletRequest}; must be {@code multipart/form-data}
     *                 for upload actions
     * @param response the {@link HttpServletResponse}
     * @throws ServletException if the request cannot be handled
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "APPLICANT")) {
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            handleDelete(request, response);
        } else {
            handleUpload(request, response);
        }
    }

    // -------------------------------------------------------------------------
    // Upload logic
    // -------------------------------------------------------------------------

    /**
     * Validates the uploaded file and persists it to the CV directory.
     * Updates the applicant's {@code cvFileName} and {@code cvFileExt} fields and
     * records an {@link ActivityLogService#UPLOAD_CV} log entry on success.
     *
     * @param request  the multipart HTTP request
     * @param response the HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException      if file I/O fails
     */
    private void handleUpload(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Part filePart = request.getPart("cvFile");
        if (filePart == null || filePart.getSize() == 0) {
            redirectWithMsg(request, response, "error_empty");
            return;
        }

        // --- size check ---
        if (filePart.getSize() > MAX_FILE_SIZE) {
            redirectWithMsg(request, response, "error_size");
            return;
        }

        // --- extension check ---
        String originalName = getSubmittedFileName(filePart);
        if (originalName == null || originalName.isEmpty()) {
            redirectWithMsg(request, response, "error_empty");
            return;
        }
        String ext = extractExtension(originalName);
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            redirectWithMsg(request, response, "error_type");
            return;
        }

        // --- MIME type check ---
        String contentType = filePart.getContentType();
        if (contentType != null && !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            redirectWithMsg(request, response, "error_type");
            return;
        }

        // --- resolve storage path ---
        Applicant applicant = (Applicant) getCurrentUser(request);
        Path cvDir = getCvDirectory(request);
        Files.createDirectories(cvDir);
        Path targetFile = cvDir.resolve(applicant.getId() + "." + ext);

        boolean hadPrevious = applicant.hasCv();

        // --- write file ---
        try (InputStream in = filePart.getInputStream()) {
            Files.copy(in, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }

        // --- delete old file if extension changed ---
        if (hadPrevious && !ext.equals(applicant.getCvFileExt())) {
            Path oldFile = cvDir.resolve(applicant.getId() + "." + applicant.getCvFileExt());
            Files.deleteIfExists(oldFile);
        }

        // --- update applicant record ---
        UserRepository userRepository = new UserRepository(getServletContext());
        applicant.setCvFileName(originalName);
        applicant.setCvFileExt(ext);
        userRepository.updateApplicant(applicant);
        request.getSession().setAttribute("currentUser", applicant);

        // --- activity log ---
        ActivityLogService logService = new ActivityLogService(getServletContext());
        logService.logUploadCv(applicant, originalName, hadPrevious);

        redirectWithMsg(request, response, hadPrevious ? "cv_replaced" : "cv_uploaded");
    }

    // -------------------------------------------------------------------------
    // Delete logic
    // -------------------------------------------------------------------------

    /**
     * Deletes the applicant's CV file from disk and clears the CV metadata on
     * their profile. Records a {@link ActivityLogService#DELETE_CV} log entry.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws IOException if file deletion or redirect fails
     */
    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Applicant applicant = (Applicant) getCurrentUser(request);
        if (!applicant.hasCv()) {
            redirectWithMsg(request, response, "error_no_cv");
            return;
        }

        // --- delete physical file ---
        Path cvDir = getCvDirectory(request);
        Path cvFile = cvDir.resolve(applicant.getId() + "." + applicant.getCvFileExt());
        Files.deleteIfExists(cvFile);

        // --- activity log (before clearing the name) ---
        ActivityLogService logService = new ActivityLogService(getServletContext());
        logService.logDeleteCv(applicant, applicant.getCvFileName());

        // --- clear metadata ---
        UserRepository userRepository = new UserRepository(getServletContext());
        applicant.setCvFileName(null);
        applicant.setCvFileExt(null);
        userRepository.updateApplicant(applicant);
        request.getSession().setAttribute("currentUser", applicant);

        redirectWithMsg(request, response, "cv_deleted");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Returns the path to the CV storage directory, creating it if necessary.
     *
     * @param request the current servlet request (used to obtain the servlet context)
     * @return absolute {@link Path} to {@code WEB-INF/data/cvs/}
     */
    private Path getCvDirectory(HttpServletRequest request) {
        String dataDir = (String) getServletContext().getAttribute(PathUtil.DATA_DIR_ATTRIBUTE);
        if (dataDir == null) {
            PathUtil.initializeDataDirectory(getServletContext());
            dataDir = (String) getServletContext().getAttribute(PathUtil.DATA_DIR_ATTRIBUTE);
        }
        return Paths.get(dataDir, CV_DIR);
    }

    /**
     * Extracts the lower-case file extension from a filename.
     *
     * @param filename the file name (e.g. {@code "MyResume.PDF"})
     * @return lower-case extension without dot (e.g. {@code "pdf"}), or empty string if none
     */
    private String extractExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(dotIndex + 1).toLowerCase();
    }

    /**
     * Extracts the original filename submitted by the browser from a multipart {@link Part}.
     * Handles both {@code filename="…"} and {@code filename*=UTF-8''…} header variants.
     *
     * @param part the multipart file part
     * @return the plain filename, or {@code null} if it cannot be determined
     */
    private String getSubmittedFileName(Part part) {
        String header = part.getHeader("content-disposition");
        if (header == null) {
            return null;
        }
        for (String token : header.split(";")) {
            token = token.trim();
            if (token.startsWith("filename=")) {
                String raw = token.substring("filename=".length()).trim();
                // strip surrounding quotes
                if (raw.startsWith("\"") && raw.endsWith("\"")) {
                    raw = raw.substring(1, raw.length() - 1);
                }
                // strip path component (IE sends full path)
                int lastSlash = Math.max(raw.lastIndexOf('/'), raw.lastIndexOf('\\'));
                return lastSlash >= 0 ? raw.substring(lastSlash + 1) : raw;
            }
        }
        return null;
    }

    /**
     * Redirects back to the applicant profile page with a message query parameter.
     *
     * @param request  the current request
     * @param response the current response
     * @param msg      the message code to append as {@code ?msg=<msg>}
     * @throws IOException if the redirect fails
     */
    private void redirectWithMsg(HttpServletRequest request, HttpServletResponse response, String msg)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/applicant/profile?msg=" + msg);
    }
}
