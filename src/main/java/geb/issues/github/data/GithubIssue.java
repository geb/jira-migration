package geb.issues.github.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class GithubIssue {

    private final OffsetDateTime createdAt;
    private final boolean closed;
    private final String title;
    private final String body;
    private final String assignee;
    private final List<String> labels;
    private final Integer milestone;

    public GithubIssue(
            String title,
            String body,
            String assignee,
            List<String> labels,
            Integer milestone,
            OffsetDateTime createdAt,
            boolean closed) {
        this.title = title;
        this.body = body;
        this.assignee = assignee;
        this.labels = labels;
        this.milestone = milestone;
        this.createdAt = createdAt;
        this.closed = closed;
    }

    @JsonProperty("created_at")
    @JsonInclude(NON_NULL)
    public String getCreatedAt() {
        return createdAt != null ? createdAt.toString() : null;
    }

    public boolean isClosed() {
        return closed;
    }

    public String getTitle() {
        return title;
    }

    @JsonInclude(NON_NULL)
    public String getBody() {
        return body;
    }

    @JsonInclude(NON_NULL)
    public String getAssignee() {
        return assignee;
    }

    public List<String> getLabels() {
        return labels;
    }

    @JsonInclude(NON_NULL)
    public Integer getMilestone() {
        return milestone;
    }
}
