package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.*;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.service.ApplicantRankingService;
import com.bupt.tarecruitment.service.ApplicationService;
import com.bupt.tarecruitment.service.JobService;
import com.bupt.tarecruitment.service.LoadProjectionService;
import com.bupt.tarecruitment.service.MatchService;
import com.bupt.tarecruitment.service.ShortlistPlannerService;
import com.bupt.tarecruitment.service.WorkloadService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Displays applications for a single MO-owned job.
 *
 * <p>Besides the basic application list, the servlet enriches each row with
 * match scores, ranking output, shortlist guidance, and workload projection
 * data so that MOs can make review decisions from one page.</p>
 *
 * <p>URL mapping: {@code /mo/applications}. The request must contain a
 * {@code jobId} query parameter and the current MO must own that job.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     ApplicantRankingService
 * @see     ShortlistPlannerService
 * @see     LoadProjectionService
 * @see     ApplicationDisplay
 */
public class MOApplicationsServlet extends BaseServlet {
    /**
     * Loads and ranks all applications for the requested MO-owned job.
     *
     * @param request  HTTP request containing the {@code jobId} query parameter
     * @param response HTTP response used for redirects, errors, or JSP forwarding
     * @throws ServletException if forwarding to the JSP view fails
     * @throws IOException      if redirecting or forwarding fails
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "MO")) {
            return;
        }

        String jobId = request.getParameter("jobId");
        JobService jobService = new JobService(getServletContext());
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            forwardError(request, response, "The selected job does not exist.", request.getContextPath() + "/mo/jobs");
            return;
        }

        if (!getCurrentUser(request).getId().equals(job.getPostedByMoId())) {
            forwardError(request, response, "You can only view applications for your own jobs.",
                    request.getContextPath() + "/mo/jobs");
            return;
        }

        ApplicationService applicationService = new ApplicationService(getServletContext());
        UserRepository userRepository = new UserRepository(getServletContext());
        MatchService matchService = new MatchService();
        ApplicantRankingService rankingService = new ApplicantRankingService();
        ShortlistPlannerService shortlistPlannerService = new ShortlistPlannerService();
        LoadProjectionService loadProjectionService = new LoadProjectionService();
        WorkloadService workloadService = new WorkloadService(getServletContext());
        List<ApplicationDisplay> displays = new ArrayList<>();
        List<ApplicationRecord> records = applicationService.getApplicationsByJob(jobId);
        List<Applicant> applicants = new ArrayList<>();

        for (ApplicationRecord record : records) {
            Applicant applicant = userRepository.findApplicantById(record.getApplicantId());
            if (applicant != null) {
                applicants.add(applicant);
            }
        }

        Map<String, ApplicationPriorityView> priorityByApplicantId =
                mapPriorityByApplicantId(rankingService.rankApplicants(job, applicants));
        Map<String, CandidateFitSnapshot> fitByApplicantId =
                mapFitByApplicantId(shortlistPlannerService.createShortlist(job, applicants));
        Map<String, Integer> currentHoursByApplicantId =
                mapCurrentHoursByApplicantId(workloadService.getApplicantWorkloadSummaries());

        for (ApplicationRecord record : records) {
            Applicant applicant = userRepository.findApplicantById(record.getApplicantId());
            MatchResult matchResult = applicant == null
                    ? new MatchResult()
                    : matchService.calculateMatch(applicant.getSkills(), job.getRequiredSkills());
            ApplicationDisplay display = new ApplicationDisplay(record, job, applicant, matchResult);
            if (applicant != null) {
                display.setPriorityView(priorityByApplicantId.get(applicant.getId()));
                display.setFitSnapshot(fitByApplicantId.get(applicant.getId()));
                display.setLoadProjection(loadProjectionService.project(
                        job,
                        applicant,
                        adjustedCurrentHours(currentHoursByApplicantId, applicant.getId(), record, job)
                ));
            }
            displays.add(display);
        }

        displays.sort(Comparator
                .comparingDouble((ApplicationDisplay display) -> priorityScore(display)).reversed()
                .thenComparing(Comparator.comparingDouble((ApplicationDisplay display) -> fitScore(display)).reversed())
                .thenComparing(display -> safeApplicantName(display.getApplicant())));
        request.setAttribute("job", job);
        request.setAttribute("applicationDisplays", displays);
        forwardView(request, response, "mo-applications.jsp");
    }

    /**
     * Indexes priority views by applicant ID for quick display-model assembly.
     *
     * @param priorityViews ranking results returned by {@link ApplicantRankingService}
     * @return map keyed by applicant ID
     */
    private Map<String, ApplicationPriorityView> mapPriorityByApplicantId(List<ApplicationPriorityView> priorityViews) {
        Map<String, ApplicationPriorityView> result = new HashMap<>();
        for (ApplicationPriorityView priorityView : priorityViews) {
            result.put(priorityView.getApplicantId(), priorityView);
        }
        return result;
    }

    /**
     * Indexes shortlist snapshots by applicant ID for quick display-model assembly.
     *
     * @param fitSnapshots shortlist results returned by {@link ShortlistPlannerService}
     * @return map keyed by applicant ID
     */
    private Map<String, CandidateFitSnapshot> mapFitByApplicantId(List<CandidateFitSnapshot> fitSnapshots) {
        Map<String, CandidateFitSnapshot> result = new HashMap<>();
        for (CandidateFitSnapshot fitSnapshot : fitSnapshots) {
            if (fitSnapshot.getApplicant() != null) {
                result.put(fitSnapshot.getApplicant().getId(), fitSnapshot);
            }
        }
        return result;
    }

    /**
     * Indexes current accepted workload hours by applicant ID.
     *
     * @param workloadSummaries workload summaries returned by {@link WorkloadService}
     * @return map from applicant ID to current accepted workload hours
     */
    private Map<String, Integer> mapCurrentHoursByApplicantId(List<WorkloadSummary> workloadSummaries) {
        Map<String, Integer> result = new HashMap<>();
        for (WorkloadSummary summary : workloadSummaries) {
            if (summary.getApplicant() != null) {
                result.put(summary.getApplicant().getId(), summary.getTotalHours());
            }
        }
        return result;
    }

    /**
     * Removes the current job's hours from already accepted rows before projecting
     * the same applicant's workload, avoiding double-counting during display.
     *
     * @param currentHoursByApplicantId current accepted hours keyed by applicant ID
     * @param applicantId               applicant being displayed
     * @param record                    application record being displayed
     * @param job                       job related to the application
     * @return adjusted workload map for projection calculation
     */
    private Map<String, Integer> adjustedCurrentHours(Map<String, Integer> currentHoursByApplicantId,
                                                      String applicantId,
                                                      ApplicationRecord record,
                                                      Job job) {
        Map<String, Integer> adjustedHours = new HashMap<>(currentHoursByApplicantId);
        if ("Accepted".equalsIgnoreCase(record.getStatus())) {
            int currentHours = Math.max(adjustedHours.getOrDefault(applicantId, 0), 0);
            adjustedHours.put(applicantId, Math.max(currentHours - Math.max(job.getHours(), 0), 0));
        }
        return adjustedHours;
    }

    /**
     * Returns the ranking score used by the table sort, or a low sentinel value.
     *
     * @param display application display row
     * @return priority score, or {@code -1.0} when no ranking data exists
     */
    private double priorityScore(ApplicationDisplay display) {
        return display.getPriorityView() == null ? -1.0 : display.getPriorityView().getPriorityScore();
    }

    /**
     * Returns the shortlist fit score used as the secondary table sort key.
     *
     * @param display application display row
     * @return fit score, or {@code -1.0} when no shortlist data exists
     */
    private double fitScore(ApplicationDisplay display) {
        return display.getFitSnapshot() == null ? -1.0 : display.getFitSnapshot().getFitScore();
    }

    /**
     * Provides a stable final sort key when scores tie.
     *
     * @param applicant applicant associated with the row
     * @return applicant full name, or an empty string when unavailable
     */
    private String safeApplicantName(Applicant applicant) {
        return applicant == null || applicant.getFullName() == null ? "" : applicant.getFullName();
    }
}
