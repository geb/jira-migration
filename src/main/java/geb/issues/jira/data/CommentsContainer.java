package geb.issues.jira.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CommentsContainer {
    private final List<JiraComment> comments;

    public CommentsContainer(@JsonProperty("comments") List<JiraComment> comments) {
        this.comments = comments;
    }

    public List<JiraComment> getComments() {
        return comments;
    }
}
