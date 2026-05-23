package com.bupt.tarecruitment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An immutable snapshot of a candidate's fit for a specific job.
 *
 * <p>A {@code CandidateFitSnapshot} is produced by
 * {@link com.bupt.tarecruitment.service.ApplicantRankingService} and represents
 * a detailed assessment of how well a given applicant matches a job posting.
 * It complements the lightweight {@link MatchResult} by adding workload
 * context and a next-step recommendation.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.ApplicantRankingService
 * @see     MatchResult
 */
public class CandidateFitSnapshot {

    /** The applicant being assessed. */
    private final Applicant applicant;

    /**
     * Overall fit score in the range [0.0, 100.0].
     * Combines skill match and workload penalty.
     */
    private final double fitScore;

    /** Skills the applicant has that align with job requirements. */
    private final List<String> alignedSkills;

    /** Required skills the applicant is missing. */
    private final List<String> gapSkills;

    /**
     * Recommended next step for this candidate.
     * Typical values: {@code "Accept"}, {@code "Review"}, {@code "Reject"}.
     * Defaults to {@code "Review"} if {@code null} is provided.
     */
    private final String nextStep;

    /**
     * Constructs an immutable {@code CandidateFitSnapshot}.
     *
     * @param applicant     the applicant
     * @param fitScore      overall fit score (0.0 – 100.0)
     * @param alignedSkills skills that match job requirements; {@code null} becomes empty list
     * @param gapSkills     skills the applicant is missing; {@code null} becomes empty list
     * @param nextStep      next-step recommendation; {@code null} defaults to {@code "Review"}
     */
    public CandidateFitSnapshot(Applicant applicant, double fitScore, List<String> alignedSkills,
                                List<String> gapSkills, String nextStep) {
        this.applicant = applicant;
        this.fitScore = fitScore;
        this.alignedSkills = alignedSkills == null ? new ArrayList<>() : new ArrayList<>(alignedSkills);
        this.gapSkills = gapSkills == null ? new ArrayList<>() : new ArrayList<>(gapSkills);
        this.nextStep = nextStep == null ? "Review" : nextStep;
    }

    /**
     * Returns the applicant being assessed.
     *
     * @return the {@link Applicant}
     */
    public Applicant getApplicant() { return applicant; }

    /**
     * Returns the overall fit score.
     *
     * @return fit score (0.0 – 100.0)
     */
    public double getFitScore() { return fitScore; }

    /**
     * Returns a defensive copy of the aligned (matching) skills list.
     *
     * @return list of aligned skill strings
     */
    public List<String> getAlignedSkills() { return new ArrayList<>(alignedSkills); }

    /**
     * Returns a defensive copy of the gap (missing) skills list.
     *
     * @return list of missing skill strings
     */
    public List<String> getGapSkills() { return new ArrayList<>(gapSkills); }

    /**
     * Returns the recommended next step for this candidate.
     *
     * @return next-step string (e.g. {@code "Accept"}, {@code "Review"}, {@code "Reject"})
     */
    public String getNextStep() { return nextStep; }
}
