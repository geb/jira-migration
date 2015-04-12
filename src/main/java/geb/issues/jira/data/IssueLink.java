package geb.issues.jira.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import geb.issues.jira.data.jackson.IssueLinkIssueDeserializer;
import geb.issues.jira.data.jackson.IssueLinkType;

public class IssueLink {
    private final String relation;
    private final Integer issue;

    public IssueLink(
            @JsonProperty("type") IssueLinkType issueLinkType,
            @JsonProperty("inwardIssue") @JsonDeserialize(using = IssueLinkIssueDeserializer.class) Integer inwardIssue,
            @JsonProperty("outwardIssue") @JsonDeserialize(using = IssueLinkIssueDeserializer.class) Integer outwardIssue) {
        if (inwardIssue != null) {
            issue = inwardIssue;
            relation = issueLinkType.getInward();
        } else {
            issue = outwardIssue;
            relation = issueLinkType.getOutward();
        }
    }

    public String getRelation() {
        return relation;
    }

    public Integer getIssue() {
        return issue;
    }
}
