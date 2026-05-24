<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="AI Skill Learning Path" />
<c:set var="pageTitleKey" value="page.skillRecommend" />
<%@ include file="includes/header.jspf" %>

<style>
/* ── Skill Recommend page ─────────────────────────────────────────────────── */
.recommend-hero {
    background: linear-gradient(135deg, rgba(15,118,110,0.13) 0%, rgba(59,130,246,0.08) 100%);
    border: 1px solid rgba(15,118,110,0.25);
    border-radius: var(--radius-xl);
    padding: 28px 32px;
    display: flex;
    align-items: flex-start;
    gap: 20px;
}
.recommend-hero-icon {
    flex-shrink: 0;
    width: 52px;
    height: 52px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(15,118,110,0.12);
    border-radius: 14px;
    color: #0f766e;
}
.recommend-hero h2 {
    margin: 0 0 8px;
    font-size: 1.45rem;
}
.recommend-hero p {
    margin: 0;
    color: var(--text-subtle);
    line-height: 1.55;
}
.ai-notice {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    margin-top: 12px;
    padding: 6px 14px;
    border-radius: 999px;
    font-size: 12px;
    font-weight: 700;
    letter-spacing: 0.03em;
}
.ai-notice svg { flex-shrink: 0; }
.ai-notice.ai-powered {
    background: rgba(15,118,110,0.12);
    color: #0f766e;
    border: 1px solid rgba(15,118,110,0.3);
}
.ai-notice.ai-fallback {
    background: rgba(180,131,27,0.1);
    color: #92400e;
    border: 1px solid rgba(180,131,27,0.25);
}

/* match summary strip */
.match-strip {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 14px;
    padding: 18px 24px;
    border-radius: var(--radius-xl);
    background: var(--surface);
    border: 1px solid rgba(255,255,255,0.7);
    box-shadow: var(--shadow-lg);
}
.match-strip-label {
    font-size: 13px;
    color: var(--text-subtle);
    text-transform: uppercase;
    letter-spacing: 0.1em;
    font-weight: 700;
}
.score-pill {
    padding: 8px 20px;
    border-radius: 999px;
    font-size: 1.15rem;
    font-weight: 800;
    letter-spacing: -0.02em;
}
.score-pill.high   { background: var(--success-soft); color: var(--success-text); }
.score-pill.medium { background: var(--warning-soft); color: var(--warning-text); }
.score-pill.low    { background: var(--danger-soft);  color: var(--danger-text);  }

.skill-tag {
    display: inline-flex;
    align-items: center;
    padding: 4px 10px;
    border-radius: 999px;
    font-size: 12px;
    font-weight: 600;
    gap: 5px;
}
.skill-tag svg { flex-shrink: 0; }
.skill-tag.matched { background: var(--success-soft); color: var(--success-text); }
.skill-tag.missing { background: var(--danger-soft);  color: var(--danger-text);  }

/* recommendation cards */
.rec-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
    gap: 22px;
}
.rec-card {
    background: var(--surface);
    border: 1px solid rgba(255,255,255,0.7);
    border-radius: var(--radius-xl);
    box-shadow: var(--shadow-lg);
    padding: 26px 28px;
    display: flex;
    flex-direction: column;
    gap: 16px;
}
.rec-card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
}
.rec-skill-name {
    font-size: 1.15rem;
    font-weight: 800;
    color: var(--text-main);
    margin: 0;
}
.rec-hours-badge {
    flex-shrink: 0;
    padding: 5px 12px;
    border-radius: 999px;
    font-size: 11px;
    font-weight: 700;
    background: rgba(15,118,110,0.1);
    color: #0f766e;
    white-space: nowrap;
}
.rec-section-label {
    font-size: 11px;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.1em;
    color: var(--text-subtle);
    margin: 0 0 6px;
}
.rec-reason {
    font-size: 14px;
    line-height: 1.65;
    color: var(--text-subtle);
    background: var(--surface-muted);
    border-left: 3px solid var(--accent);
    padding: 12px 14px;
    border-radius: 0 12px 12px 0;
    margin: 0;
}
.learning-steps {
    list-style: none;
    margin: 0;
    padding: 0;
    display: flex;
    flex-direction: column;
    gap: 8px;
    counter-reset: step-counter;
}
.learning-steps li {
    display: flex;
    align-items: flex-start;
    gap: 10px;
    font-size: 14px;
    line-height: 1.55;
    color: var(--text-main);
    counter-increment: step-counter;
}
.learning-steps li::before {
    content: counter(step-counter);
    flex-shrink: 0;
    width: 24px;
    height: 24px;
    border-radius: 50%;
    background: var(--accent);
    color: #fff;
    font-size: 11px;
    font-weight: 700;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 2px;
}
.resource-list {
    list-style: none;
    margin: 0;
    padding: 0;
    display: flex;
    flex-direction: column;
    gap: 6px;
}
.resource-list li a {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    font-size: 13.5px;
    color: #0f766e;
    font-weight: 600;
    text-decoration: none;
    transition: color 0.15s;
}
.resource-list li a:hover {
    color: var(--accent-hover);
    text-decoration: underline;
}
.resource-list li a .link-icon { opacity: 0.65; flex-shrink: 0; }

/* all-matched banner */
.all-matched-banner {
    text-align: center;
    padding: 52px 24px;
    background: var(--success-soft);
    border: 1px dashed rgba(22,101,52,0.3);
    border-radius: var(--radius-xl);
}
.all-matched-banner .banner-icon {
    display: flex;
    justify-content: center;
    margin-bottom: 16px;
    color: var(--success-text);
}
.all-matched-banner h3 { margin: 0 0 8px; color: var(--success-text); }
.all-matched-banner p  { margin: 0; color: #166534; }

/* explainability disclaimer */
.ai-disclaimer {
    display: flex;
    align-items: flex-start;
    gap: 10px;
    font-size: 12.5px;
    color: var(--text-subtle);
    padding: 12px 18px;
    border-radius: 12px;
    background: rgba(148,163,184,0.08);
    border: 1px solid var(--line);
    line-height: 1.6;
}
.ai-disclaimer svg { flex-shrink: 0; margin-top: 2px; }
.ai-disclaimer strong { color: var(--text-main); }
</style>

<%-- ── SVG icon snippets ──────────────────────────────────────────────────── --%>
<%-- robot (hero + badge + nav) --%>
<c:set var="svgRobot">
<svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
  <rect x="3" y="11" width="18" height="10" rx="2"/>
  <circle cx="9" cy="16" r="1.5" fill="currentColor" stroke="none"/>
  <circle cx="15" cy="16" r="1.5" fill="currentColor" stroke="none"/>
  <path d="M12 11V7"/>
  <circle cx="12" cy="5" r="2"/>
  <line x1="8" y1="11" x2="8" y2="14"/>
  <line x1="16" y1="11" x2="16" y2="14"/>
</svg>
</c:set>

<%-- sparkle / AI powered --%>
<c:set var="svgSparkle">
<svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
  <path d="M12 2l2.4 7.4H22l-6.2 4.5 2.4 7.4L12 17l-6.2 4.3 2.4-7.4L2 9.4h7.6z"/>
</svg>
</c:set>

<%-- book open / curated --%>
<c:set var="svgBook">
<svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
  <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/>
  <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/>
</svg>
</c:set>

<%-- check-mark (matched skills) --%>
<c:set var="svgCheck">
<svg xmlns="http://www.w3.org/2000/svg" width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
  <polyline points="20 6 9 17 4 12"/>
</svg>
</c:set>

<%-- x-mark (missing skills) --%>
<c:set var="svgX">
<svg xmlns="http://www.w3.org/2000/svg" width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
  <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
</svg>
</c:set>

<%-- warning triangle (disclaimer) --%>
<c:set var="svgWarning">
<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
  <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
  <line x1="12" y1="9" x2="12" y2="13"/>
  <line x1="12" y1="17" x2="12.01" y2="17"/>
</svg>
</c:set>

<%-- circle-check (all matched banner) --%>
<c:set var="svgCircleCheck">
<svg xmlns="http://www.w3.org/2000/svg" width="56" height="56" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
  <circle cx="12" cy="12" r="10"/>
  <polyline points="9 12 11 14 15 10"/>
</svg>
</c:set>

<%-- external link arrow (resources) --%>
<c:set var="svgExternalLink">
<svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true" class="link-icon">
  <line x1="7" y1="17" x2="17" y2="7"/>
  <polyline points="7 7 17 7 17 17"/>
</svg>
</c:set>

<%-- arrow left (back button) --%>
<c:set var="svgArrowLeft">
<svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
  <line x1="19" y1="12" x2="5" y2="12"/>
  <polyline points="12 19 5 12 12 5"/>
</svg>
</c:set>

<!-- ── Hero / page intro ──────────────────────────────────────────────────── -->
<div class="recommend-hero">
    <div class="recommend-hero-icon">${svgRobot}</div>
    <div>
        <h2 data-i18n="skillRecommend.title">AI Skill Learning Path</h2>
        <p data-i18n="skillRecommend.subtitle">
            Personalised learning recommendations generated for your missing skills based on
            the requirements of <strong>${job.title}</strong> (${job.moduleName}).
        </p>
        <c:choose>
            <c:when test="${aiStatus == 'ai'}">
                <span class="ai-notice ai-powered">${svgSparkle} AI-Powered &ndash; Doubao LLM via Volcano Engine</span>
            </c:when>
            <c:otherwise>
                <span class="ai-notice ai-fallback">${svgBook} Curated Resources &ndash; Static Catalogue</span>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- ── Match summary strip ───────────────────────────────────────────────── -->
<div class="match-strip">
    <span class="match-strip-label" data-i18n="jobDetail.matchScore">Match Score</span>
    <c:choose>
        <c:when test="${matchResult.score >= 70}">
            <span class="score-pill high">${matchResult.score}%</span>
        </c:when>
        <c:when test="${matchResult.score >= 40}">
            <span class="score-pill medium">${matchResult.score}%</span>
        </c:when>
        <c:otherwise>
            <span class="score-pill low">${matchResult.score}%</span>
        </c:otherwise>
    </c:choose>

    <c:if test="${not empty matchResult.matchedSkills}">
        <span class="match-strip-label" data-i18n="jobDetail.matchedSkills">Matched</span>
        <c:forEach items="${matchResult.matchedSkills}" var="sk">
            <span class="skill-tag matched">${svgCheck} ${sk}</span>
        </c:forEach>
    </c:if>

    <c:if test="${not empty matchResult.missingSkills}">
        <span class="match-strip-label" data-i18n="jobDetail.missingSkills">Missing</span>
        <c:forEach items="${matchResult.missingSkills}" var="sk">
            <span class="skill-tag missing">${svgX} ${sk}</span>
        </c:forEach>
    </c:if>
</div>

<!-- ── AI Disclaimer ─────────────────────────────────────────────────────── -->
<p class="ai-disclaimer">
    ${svgWarning}
    <span>
        <strong data-i18n="skillRecommend.disclaimerTitle">AI Output Notice:</strong>
        <span data-i18n="skillRecommend.disclaimer">
            The recommendations below were generated by a large language model (LLM) and have been
            validated by structured logic before display. The <em>reason</em> field explains the AI&rsquo;s
            rationale to satisfy explainability requirements. Learning resources are verified to use
            HTTPS and checked against a curated catalogue. Always exercise your own judgement when
            following AI-generated study plans.
        </span>
    </span>
</p>

<!-- ── Main content ──────────────────────────────────────────────────────── -->
<c:choose>

    <%-- All skills matched – nothing to recommend --%>
    <c:when test="${aiStatus == 'no-missing' or empty recommendations}">
        <div class="all-matched-banner">
            <div class="banner-icon">${svgCircleCheck}</div>
            <h3 data-i18n="skillRecommend.allMatched">You have all the required skills!</h3>
            <p data-i18n="skillRecommend.allMatchedSub">
                Great news &ndash; your current skills fully satisfy the requirements for this position.
                You can apply directly without any additional preparation.
            </p>
            <br>
            <a href="${pageContext.request.contextPath}/jobs/detail?id=${job.id}"
               class="btn btn-primary" data-i18n="action.viewJob">Back to Job Detail</a>
        </div>
    </c:when>

    <%-- Show AI recommendations --%>
    <c:otherwise>
        <div class="rec-grid">
            <c:forEach items="${recommendations}" var="rec">
                <div class="rec-card">

                    <!-- Card header: skill name + estimated hours -->
                    <div class="rec-card-header">
                        <h3 class="rec-skill-name">${rec.skill}</h3>
                        <c:if test="${rec.estimatedHours > 0}">
                            <span class="rec-hours-badge">~${rec.estimatedHours}h to learn</span>
                        </c:if>
                    </div>

                    <!-- Why this skill matters (explainability) -->
                    <c:if test="${not empty rec.reason}">
                        <div>
                            <p class="rec-section-label" data-i18n="skillRecommend.whyMatters">Why this skill matters</p>
                            <p class="rec-reason">${rec.reason}</p>
                        </div>
                    </c:if>

                    <!-- Learning path steps -->
                    <c:if test="${not empty rec.learningPath}">
                        <div>
                            <p class="rec-section-label" data-i18n="skillRecommend.learningPath">Suggested Learning Path</p>
                            <ol class="learning-steps">
                                <%-- Split pipe-delimited steps --%>
                                <c:set var="rawPath" value="${rec.learningPath}" />
                                <c:forEach items="${fn:split(rawPath, '|')}" var="step">
                                    <c:set var="trimStep" value="${fn:trim(step)}" />
                                    <c:if test="${not empty trimStep}">
                                        <%-- Strip leading "1. " "2." etc. --%>
                                        <c:set var="cleanStep" value="${fn:trim(trimStep)}" />
                                        <c:if test="${fn:length(cleanStep) > 2 and fn:substring(cleanStep,1,2) == '.'}">
                                            <c:set var="cleanStep" value="${fn:trim(fn:substring(cleanStep, 3, fn:length(cleanStep)))}" />
                                        </c:if>
                                        <li>${cleanStep}</li>
                                    </c:if>
                                </c:forEach>
                            </ol>
                        </div>
                    </c:if>

                    <!-- Resource links -->
                    <c:if test="${not empty rec.resourceLinks}">
                        <div>
                            <p class="rec-section-label" data-i18n="skillRecommend.resources">Learning Resources</p>
                            <ul class="resource-list">
                                <c:forEach items="${rec.resourceLinks}" var="link">
                                    <li>
                                        <a href="${link.url}" target="_blank" rel="noopener noreferrer">
                                            ${svgExternalLink}${link.label}
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>

                </div><%-- /.rec-card --%>
            </c:forEach>
        </div><%-- /.rec-grid --%>
    </c:otherwise>

</c:choose>

<!-- ── Back button ───────────────────────────────────────────────────────── -->
<div style="margin-top: 8px; display:flex; gap:10px; align-items:center;">
    <a href="${pageContext.request.contextPath}/jobs/detail?id=${job.id}"
       class="btn btn-light" style="display:inline-flex;align-items:center;gap:6px;"
       data-i18n="action.back">${svgArrowLeft} Back to Job</a>
    <a href="${pageContext.request.contextPath}/jobs"
       class="btn btn-light" data-i18n="nav.jobs">Browse All Jobs</a>
</div>

<%@ include file="includes/footer.jspf" %>
