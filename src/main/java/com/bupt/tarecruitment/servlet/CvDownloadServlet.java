package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.repository.ApplicationRepository;
import com.bupt.tarecruitment.repository.JobRepository;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.util.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet that streams a stored CV file to an authorised requester.
 *
 * <h2>Access rules</h2>
 * <ul>
 *   <li><b>APPLICANT</b> – may download only their own CV.</li>
 *   <li><b>MO</b> – may download the CV of any applicant who has applied to
 *       one of the MO's jobs (verified by the {@code applicantId} parameter
 *       against {@link ApplicationRepository} and {@link JobRepository}).</li>
 *   <li><b>ADMIN</b> – may download any applicant's CV.</li>
 * </ul>
 *
 * <h2>Request parameter</h2>
 * <pre>GET /cv/download?applicantId={id}</pre>
 *
 * <h2>Response</h2>
 * The file is sent as a binary attachment. The {@code Content-Disposition} header
 * is set to {@code attachment; filename="{fullName}_CV.{ext}"} so the browser
 * presents a sensible save-as filename.
 *
 * @author  Group 71
 * @version 1.0
 * @see     CvUploadServlet
 */
public class CvDownloadServlet extends BaseServlet {

    /** Sub-directory inside the data directory where CV files reside. */
    private static final String CV_DIR = "cvs";

    /**
     * Maps lower-case file extensions to their canonical MIME types.
     * Used to set {@code Content-Type} on the download response.
     */
    private static final Map<String, String> MIME_MAP;

    static {
        MIME_MAP = new HashMap<>();
        MIME_MAP.put("pdf",  "application/pdf");
        MIME_MAP.put("doc",  "application/msword");
        MIME_MAP.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    }

    // -------------------------------------------------------------------------
    // HTTP handler
    // -------------------------------------------------------------------------

    /**
     * Handles the CV download request. Validates the requester's permission,
     * locates the physical file, and streams it as a binary download.
     *
     * @param request  the HTTP GET request with {@code applicantId} parameter
     * @param response the HTTP response; body is the raw file bytes on success
     * @throws ServletException if forwarding to an error view fails
     * @throws IOException      if reading the file or writing to the response fails
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!requireLogin(request, response)) {
            return;
        }

        String applicantId = request.getParameter("applicantId");
        if (applicantId == null || applicantId.trim().isEmpty()) {
            forwardError(request, response, "Missing applicantId parameter.",
                    request.getContextPath() + "/dashboard");
            return;
        }

        // --- load target applicant ---
        UserRepository userRepository = new UserRepository(getServletContext());
        Applicant applicant = userRepository.findApplicantById(applicantId);
        if (applicant == null) {
            forwardError(request, response, "Applicant not found.",
                    request.getContextPath() + "/dashboard");
            return;
        }

        if (!applicant.hasCv()) {
            forwardError(request, response, "This applicant has not uploaded a CV.",
                    request.getContextPath() + "/dashboard");
            return;
        }

        // --- authorisation ---
        User currentUser = getCurrentUser(request);
        if (!isAuthorised(currentUser, applicant, request)) {
            forwardError(request, response, "You are not authorised to download this CV.",
                    request.getContextPath() + "/dashboard");
            return;
        }

        // --- resolve physical file ---
        String dataDir = (String) getServletContext().getAttribute(PathUtil.DATA_DIR_ATTRIBUTE);
        if (dataDir == null) {
            PathUtil.initializeDataDirectory(getServletContext());
            dataDir = (String) getServletContext().getAttribute(PathUtil.DATA_DIR_ATTRIBUTE);
        }
        Path cvFile = Paths.get(dataDir, CV_DIR,
                applicant.getId() + "." + applicant.getCvFileExt());

        if (!Files.exists(cvFile)) {
            forwardError(request, response,
                    "CV file not found on server. It may have been removed.",
                    request.getContextPath() + "/dashboard");
            return;
        }

        // --- stream file ---
        String mimeType = MIME_MAP.getOrDefault(applicant.getCvFileExt(), "application/octet-stream");
        String downloadName = sanitise(applicant.getFullName()) + "_CV." + applicant.getCvFileExt();

        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadName + "\"");
        // Use setContentLength(int) for Servlet 3.0 / Tomcat 7 compatibility.
        // setContentLengthLong(long) was introduced in Servlet 3.1 and is not
        // available on the deployed Tomcat 7.0.47 runtime.
        long fileSize = Files.size(cvFile);
        response.setContentLength((int) fileSize);

        try (OutputStream out = response.getOutputStream()) {
            Files.copy(cvFile, out);
            out.flush();
        }
    }

    // -------------------------------------------------------------------------
    // Authorisation helper
    // -------------------------------------------------------------------------

    /**
     * Checks whether {@code currentUser} is permitted to download the CV of
     * {@code applicant}.
     *
     * <ul>
     *   <li>APPLICANT: only their own CV.</li>
     *   <li>MO: only if the applicant has applied to one of the MO's jobs.</li>
     *   <li>ADMIN: always permitted.</li>
     * </ul>
     *
     * @param currentUser the logged-in user
     * @param applicant   the applicant whose CV is requested
     * @param request     the current servlet request (for repository access)
     * @return {@code true} if the download is permitted
     */
    private boolean isAuthorised(User currentUser, Applicant applicant,
                                  HttpServletRequest request) {
        String role = currentUser.getRole();

        if ("ADMIN".equalsIgnoreCase(role)) {
            return true;
        }

        if ("APPLICANT".equalsIgnoreCase(role)) {
            return currentUser.getId().equals(applicant.getId());
        }

        if ("MO".equalsIgnoreCase(role)) {
            // MO can only download CV of applicants who applied to their jobs
            ApplicationRepository appRepo = new ApplicationRepository(getServletContext());
            JobRepository jobRepo = new JobRepository(getServletContext());
            List<ApplicationRecord> applications =
                    appRepo.findByApplicantId(applicant.getId());
            for (ApplicationRecord rec : applications) {
                com.bupt.tarecruitment.model.Job job = jobRepo.findById(rec.getJobId());
                if (job != null && currentUser.getId().equals(job.getPostedByMoId())) {
                    return true;
                }
            }
            return false;
        }

        return false;
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    /**
     * Sanitises a name string for safe use inside a {@code Content-Disposition}
     * header filename. Replaces any character that is not a letter, digit,
     * hyphen, or space with an underscore, then trims trailing spaces.
     *
     * @param name the raw name (e.g. applicant's full name)
     * @return a filesystem- and HTTP-header-safe version of the name
     */
    private String sanitise(String name) {
        if (name == null || name.isEmpty()) {
            return "Applicant";
        }
        return name.replaceAll("[^a-zA-Z0-9\\-_ ]", "_").trim();
    }
}
