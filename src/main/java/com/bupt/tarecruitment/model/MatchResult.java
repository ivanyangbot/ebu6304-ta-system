package com.bupt.tarecruitment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the result of a skill-matching calculation.
 *
 * <p>A {@code MatchResult} is produced by
 * {@link com.bupt.tarecruitment.service.MatchService#calculateMatch(List, List)}
 * and contains:</p>
 * <ul>
 *   <li>{@link #matchedSkills} – skills possessed by the applicant that the job requires</li>
 *   <li>{@link #missingSkills} – required skills the applicant currently lacks</li>
 *   <li>{@link #score}         – percentage match score (0.0 – 100.0, one decimal place)</li>
 * </ul>
 *
 * <p>The match score is calculated as:</p>
 * <pre>
 *   score = (matchedSkills.size() / requiredSkills.size()) * 100.0
 * </pre>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.MatchService
 */
public class MatchResult {

    /** Skills the applicant has that satisfy the job requirements. */
    private List<String> matchedSkills;

    /** Required skills that the applicant does not currently possess. */
    private List<String> missingSkills;

    /**
     * Percentage match score in the range [0.0, 100.0], rounded to one decimal place.
     * A score of 100.0 means all required skills are covered.
     */
    private double score;

    /**
     * Default no-argument constructor. Initialises both skill lists to empty lists
     * and the score to 0.0.
     */
    public MatchResult() {
        this.matchedSkills = new ArrayList<>();
        this.missingSkills = new ArrayList<>();
    }

    /**
     * Full constructor.
     *
     * @param matchedSkills skills possessed by the applicant that match job requirements;
     *                      {@code null} becomes an empty list
     * @param missingSkills required skills the applicant lacks;
     *                      {@code null} becomes an empty list
     * @param score         percentage match score (0.0 – 100.0)
     */
    public MatchResult(List<String> matchedSkills, List<String> missingSkills, double score) {
        this.matchedSkills = matchedSkills == null ? new ArrayList<>() : matchedSkills;
        this.missingSkills = missingSkills == null ? new ArrayList<>() : missingSkills;
        this.score = score;
    }

    /**
     * Returns the list of skills that were matched.
     *
     * @return non-null list of matched skill strings
     */
    public List<String> getMatchedSkills() {
        if (matchedSkills == null) {
            matchedSkills = new ArrayList<>();
        }
        return matchedSkills;
    }

    /**
     * Sets the list of matched skills.
     *
     * @param matchedSkills matched skill list; {@code null} becomes empty list
     */
    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills == null ? new ArrayList<>() : matchedSkills;
    }

    /**
     * Returns the list of required skills that were not matched.
     *
     * @return non-null list of missing skill strings
     */
    public List<String> getMissingSkills() {
        if (missingSkills == null) {
            missingSkills = new ArrayList<>();
        }
        return missingSkills;
    }

    /**
     * Sets the list of missing skills.
     *
     * @param missingSkills missing skill list; {@code null} becomes empty list
     */
    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills == null ? new ArrayList<>() : missingSkills;
    }

    /**
     * Returns the percentage match score.
     *
     * @return score in the range [0.0, 100.0]
     */
    public double getScore() {
        return score;
    }

    /**
     * Sets the percentage match score.
     *
     * @param score new score value (0.0 – 100.0)
     */
    public void setScore(double score) {
        this.score = score;
    }
}
