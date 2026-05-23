package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.WorkloadSummary;
import com.bupt.tarecruitment.repository.ApplicationRepository;
import com.bupt.tarecruitment.repository.JobRepository;
import com.bupt.tarecruitment.repository.UserRepository;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.StringJoiner;

/**
 * Builds a compact text snapshot of the current recruitment data store.
 *
 * <p>The snapshot is injected into the AI prompt so administrators can ask
 * natural-language questions about users, jobs, applications, and workload
 * without exposing raw JSON files to the browser.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.DeepSeekAiService
 * @see     com.bupt.tarecruitment.servlet.AdminAiQueryStreamServlet
 */
public class RecruitmentDataSnapshotService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final WorkloadService workloadService;

    /**
     * Creates a snapshot service bound to the current servlet context.
     *
     * @param servletContext running application context used to locate JSON files
     */
    public RecruitmentDataSnapshotService(ServletContext servletContext) {
        this.userRepository = new UserRepository(servletContext);
        this.jobRepository = new JobRepository(servletContext);
        this.applicationRepository = new ApplicationRepository(servletContext);
        this.workloadService = new WorkloadService(servletContext);
    }

    /**
     * Builds a human-readable summary of the current system data.
     *
     * @return plain-text snapshot suitable for inclusion in an AI prompt
     */
    public String buildSnapshot() {
        List<Job> jobs = jobRepository.findAll();
        List<ApplicationRecord> applications = applicationRepository.findAll();
        List<WorkloadSummary> workloadSummaries = workloadService.getApplicantWorkloadSummaries();

        int applicantCount = userRepository.findAllApplicants().size();
        int moCount = userRepository.findAllMOs().size();
        int adminCount = userRepository.findAllAdmins().size();
        int openJobCount = jobRepository.findAllOpen().size();
        int pendingCount = countApplicationsByStatus(applications, "Pending");
        int acceptedCount = countApplicationsByStatus(applications, "Accepted");
        int rejectedCount = countApplicationsByStatus(applications, "Rejected");
        int overloadedCount = countOverloaded(workloadSummaries);

        StringBuilder builder = new StringBuilder();
        builder.append("TA Recruitment System Data Snapshot\n");
        builder.append("- Users: ").append(applicantCount + moCount + adminCount)
                .append(" total (Applicants=").append(applicantCount)
                .append(", MOs=").append(moCount)
                .append(", Admins=").append(adminCount).append(")\n");
        builder.append("- Jobs: ").append(jobs.size())
                .append(" total, Open=").append(openJobCount).append("\n");
        builder.append("- Applications: ").append(applications.size())
                .append(" total, Pending=").append(pendingCount)
                .append(", Accepted=").append(acceptedCount)
                .append(", Rejected=").append(rejectedCount).append("\n");
        builder.append("- Overloaded applicants: ").append(overloadedCount).append("\n");
        builder.append("- Open job titles: ").append(joinOpenJobTitles(jobs)).append("\n");
        builder.append("- Overloaded applicant names: ").append(joinOverloadedNames(workloadSummaries));
        return builder.toString();
    }

    /**
     * Counts application records with the given status label.
     *
     * @param applications all application records
     * @param status       status label to match, case-insensitive
     * @return number of matching records
     */
    private int countApplicationsByStatus(List<ApplicationRecord> applications, String status) {
        int count = 0;
        for (ApplicationRecord record : applications) {
            if (status.equalsIgnoreCase(record.getStatus())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts applicants whose workload status is {@code Overloaded}.
     *
     * @param workloadSummaries applicant workload summaries
     * @return overloaded applicant count
     */
    private int countOverloaded(List<WorkloadSummary> workloadSummaries) {
        int count = 0;
        for (WorkloadSummary summary : workloadSummaries) {
            if ("Overloaded".equalsIgnoreCase(summary.getWorkloadStatus())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Joins open job titles into a comma-separated list for the snapshot.
     *
     * @param jobs all jobs in the system
     * @return comma-separated open job titles, or {@code None}
     */
    private String joinOpenJobTitles(List<Job> jobs) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Job job : jobs) {
            if ("Open".equalsIgnoreCase(job.getStatus())) {
                joiner.add(job.getTitle());
            }
        }
        return joiner.length() == 0 ? "None" : joiner.toString();
    }

    /**
     * Joins overloaded applicant names into a comma-separated list.
     *
     * @param workloadSummaries applicant workload summaries
     * @return comma-separated applicant names, or {@code None}
     */
    private String joinOverloadedNames(List<WorkloadSummary> workloadSummaries) {
        StringJoiner joiner = new StringJoiner(", ");
        for (WorkloadSummary summary : workloadSummaries) {
            if ("Overloaded".equalsIgnoreCase(summary.getWorkloadStatus())
                    && summary.getApplicant() != null) {
                joiner.add(summary.getApplicant().getFullName());
            }
        }
        return joiner.length() == 0 ? "None" : joiner.toString();
    }
}
