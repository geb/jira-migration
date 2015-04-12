package geb.issues.jira.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JiraIssue {
    private final IssueFields fields;

    public JiraIssue(@JsonProperty("fields") IssueFields fields) {
        this.fields = fields;
    }

    public IssueFields getFields() {
        return fields;
    }
}
