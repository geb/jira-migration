package geb.issues.jira.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import geb.issues.github.data.AsLabel;
import geb.issues.github.data.Label;
import geb.issues.jira.data.jackson.IssueResolutionDeserializer;

@JsonDeserialize(using = IssueResolutionDeserializer.class)
public enum IssueResolution implements AsLabel {
    NOT_A_BUG("Not a bug"), CANNOT_REPRODUCE("Cannot reproduce"), INCOMPLETE("Incomplete"), DUPLICATE("Duplicate"), WONT_FIX("Won't fix"), FIXED;

    private final Label label;

    IssueResolution() {
        label = null;
    }

    IssueResolution(String name) {
        label = new Label(name, "fbca04");
    }

    public Label asLabel() {
        return label;
    }
}
