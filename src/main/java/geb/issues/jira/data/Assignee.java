package geb.issues.jira.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import geb.issues.jira.data.jackson.AssigneeDeserializer;

@JsonDeserialize(using = AssigneeDeserializer.class)
public enum Assignee {
    MARCIN_ERDMANN("erdi"), LUKE_DALEY("alkemist"), CHRIS_PRIOR("chris-prior"), ROBERT_FLETCHER(null);

    private final String gitHubUserName;

    Assignee(String gitHubUserName) {
        this.gitHubUserName = gitHubUserName;
    }

    public String getGitHubUserName() {
        return gitHubUserName;
    }
}
