package geb.issues.jira.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import geb.issues.github.data.Label;
import geb.issues.jira.data.jackson.CustomOffsetDateTimeDeserializer;
import geb.issues.jira.data.jackson.FixVersionsDeserializer;
import geb.issues.jira.data.jackson.DisplayNameDeserializer;
import geb.issues.jira.data.jackson.IssueTypeDeserializer;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.isEqual;
import static java.util.stream.Collectors.toList;

public class IssueFields {
    private final String summary;
    private final String description;
    private final IssueResolution resolution;
    private final String fixVersion;
    private final String reporter;
    private final OffsetDateTime created;
    private final Assignee assignee;
    private final CommentsContainer commentsContainer;
    private final IssueType issueType;
    private final List<IssueLink> issueLinks;
    private final OffsetDateTime resolved;


    public IssueFields(
            @JsonProperty("summary") String summary,
            @JsonProperty("description") String description,
            @JsonProperty("resolution") IssueResolution resolution,
            @JsonProperty("fixVersions") @JsonDeserialize(using = FixVersionsDeserializer.class) String fixVersion,
            @JsonProperty("reporter") @JsonDeserialize(using = DisplayNameDeserializer.class) String reporter,
            @JsonProperty("created") @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class) OffsetDateTime created,
            @JsonProperty("assignee") Assignee assignee,
            @JsonProperty("comment") CommentsContainer commentsContainer,
            @JsonProperty("issuetype") @JsonDeserialize(using = IssueTypeDeserializer.class) IssueType issueType,
            @JsonProperty("issuelinks") List<IssueLink> issueLinks,
            @JsonProperty("resolutiondate") @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class) OffsetDateTime resolved) {
        this.summary = summary;
        this.description = description;
        this.resolution = resolution;
        this.fixVersion = fixVersion;
        this.reporter = reporter;
        this.created = created;
        this.assignee = assignee;
        this.commentsContainer = commentsContainer;
        this.issueType = issueType;
        this.issueLinks = issueLinks;
        this.resolved = resolved;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public IssueResolution getResolution() {
        return resolution;
    }

    public String getFixVersion() {
        return fixVersion;
    }

    public String getReporter() {
        return reporter;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public List<JiraComment> getComments() {
        return commentsContainer.getComments();
    }

    public IssueType getIssueType() {
        return issueType;
    }

    @JsonIgnore
    public List<String> getLabels() {
        Stream<Label> labels = Stream.of(resolution == null ? null : resolution.asLabel(), issueType.asLabel());
        return labels.filter(isEqual(null).negate()).map(Label::getName).collect(toList());
    }

    public List<IssueLink> getIssueLinks() {
        return issueLinks;
    }

    public OffsetDateTime getResolved() {
        return resolved;
    }
}
