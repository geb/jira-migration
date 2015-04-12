package geb.issues.github.data;

import java.util.List;

public class Import {
    private final GithubIssue issue;
    private final List<GithubComment> comments;

    public Import(GithubIssue issue, List<GithubComment> comments) {
        this.issue = issue;
        this.comments = comments;
    }

    public GithubIssue getIssue() {
        return issue;
    }

    public List<GithubComment> getComments() {
        return comments;
    }
}
