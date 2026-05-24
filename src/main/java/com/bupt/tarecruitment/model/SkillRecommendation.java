package com.bupt.tarecruitment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an AI-generated learning recommendation for a single missing skill.
 *
 * <p>A {@code SkillRecommendation} is produced by
 * {@link com.bupt.tarecruitment.service.AiRecommendationService} when an applicant
 * is missing one or more skills required for a job. Each instance covers one skill
 * and contains:</p>
 * <ul>
 *   <li>{@link #skill}          – the name of the missing skill</li>
 *   <li>{@link #reason}         – a short, AI-generated explanation of why the skill
 *                                  matters for the role (explainable AI requirement)</li>
 *   <li>{@link #learningPath}   – a concise step-by-step study plan (2-4 steps)</li>
 *   <li>{@link #resourceLinks}  – a list of curated {@link ResourceLink} objects pointing
 *                                  to free online learning materials</li>
 *   <li>{@link #estimatedHours} – AI-estimated total study hours to reach competency</li>
 * </ul>
 *
 * <p>The {@code reason} and {@code learningPath} fields are verified by structured logic
 * before being rendered in the UI to guard against hallucinated or irrelevant responses
 * (see {@link com.bupt.tarecruitment.service.AiRecommendationService#sanitise(String)}).</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.AiRecommendationService
 */
public class SkillRecommendation {

    /** The name of the skill that the applicant is missing. */
    private String skill;

    /**
     * AI-generated explanation of why this skill is important for the target role.
     * Used to satisfy the "explainable AI" requirement of the coursework.
     */
    private String reason;

    /**
     * A short structured learning path (2-4 steps) produced by the AI and
     * sanitised by {@link com.bupt.tarecruitment.service.AiRecommendationService}.
     */
    private String learningPath;

    /**
     * Curated list of free online resources for learning this skill.
     * Each entry holds a display label and a URL.
     */
    private List<ResourceLink> resourceLinks;

    /**
     * AI-estimated number of study hours needed to reach a working knowledge
     * of this skill. Defaults to 0 when the AI does not provide an estimate.
     */
    private int estimatedHours;

    /**
     * Default no-argument constructor. Initialises {@code resourceLinks} to an
     * empty list and numeric fields to zero.
     */
    public SkillRecommendation() {
        this.resourceLinks = new ArrayList<>();
        this.estimatedHours = 0;
    }

    /**
     * Full constructor.
     *
     * @param skill          name of the missing skill
     * @param reason         AI explanation of why the skill is needed
     * @param learningPath   structured 2-4 step study plan
     * @param resourceLinks  list of curated learning resource links; {@code null} becomes empty list
     * @param estimatedHours estimated total study hours (≥ 0)
     */
    public SkillRecommendation(String skill, String reason, String learningPath,
                               List<ResourceLink> resourceLinks, int estimatedHours) {
        this.skill = skill;
        this.reason = reason;
        this.learningPath = learningPath;
        this.resourceLinks = resourceLinks == null ? new ArrayList<>() : resourceLinks;
        this.estimatedHours = estimatedHours;
    }

    /**
     * Returns the name of the missing skill.
     *
     * @return skill name string
     */
    public String getSkill() {
        return skill;
    }

    /**
     * Sets the name of the missing skill.
     *
     * @param skill skill name
     */
    public void setSkill(String skill) {
        this.skill = skill;
    }

    /**
     * Returns the AI-generated reason this skill matters.
     *
     * @return reason text
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason text.
     *
     * @param reason AI-generated reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Returns the structured learning path.
     *
     * @return learning path description
     */
    public String getLearningPath() {
        return learningPath;
    }

    /**
     * Sets the structured learning path.
     *
     * @param learningPath structured study plan
     */
    public void setLearningPath(String learningPath) {
        this.learningPath = learningPath;
    }

    /**
     * Returns the list of curated resource links.
     * Lazily initialises the list if it is {@code null}.
     *
     * @return non-null list of {@link ResourceLink} objects
     */
    public List<ResourceLink> getResourceLinks() {
        if (resourceLinks == null) {
            resourceLinks = new ArrayList<>();
        }
        return resourceLinks;
    }

    /**
     * Replaces the resource link list.
     *
     * @param resourceLinks new list; {@code null} becomes an empty list
     */
    public void setResourceLinks(List<ResourceLink> resourceLinks) {
        this.resourceLinks = resourceLinks == null ? new ArrayList<>() : resourceLinks;
    }

    /**
     * Returns the estimated study hours.
     *
     * @return hours as a non-negative integer
     */
    public int getEstimatedHours() {
        return estimatedHours;
    }

    /**
     * Sets the estimated study hours.
     *
     * @param estimatedHours estimated hours (should be ≥ 0)
     */
    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours = Math.max(0, estimatedHours);
    }

    // -------------------------------------------------------------------------
    // Nested type
    // -------------------------------------------------------------------------

    /**
     * A single learning resource with a human-readable label and a URL.
     *
     * <p>Instances are constructed either from the AI response or from the static
     * fallback catalogue maintained in
     * {@link com.bupt.tarecruitment.service.AiRecommendationService}.</p>
     */
    public static class ResourceLink {

        /** Human-readable display text shown as the hyperlink label. */
        private String label;

        /** Absolute URL of the resource (always HTTPS). */
        private String url;

        /**
         * Default no-argument constructor required for JSON deserialisation.
         */
        public ResourceLink() {
        }

        /**
         * Convenience constructor.
         *
         * @param label display text
         * @param url   absolute resource URL
         */
        public ResourceLink(String label, String url) {
            this.label = label;
            this.url = url;
        }

        /**
         * Returns the display label.
         *
         * @return label text
         */
        public String getLabel() {
            return label;
        }

        /**
         * Sets the display label.
         *
         * @param label display text
         */
        public void setLabel(String label) {
            this.label = label;
        }

        /**
         * Returns the resource URL.
         *
         * @return absolute URL string
         */
        public String getUrl() {
            return url;
        }

        /**
         * Sets the resource URL.
         *
         * @param url absolute URL string
         */
        public void setUrl(String url) {
            this.url = url;
        }
    }
}
