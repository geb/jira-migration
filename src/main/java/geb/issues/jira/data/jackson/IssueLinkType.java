package geb.issues.jira.data.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IssueLinkType {
    private final String inward;
    private final String outward;

    public IssueLinkType(@JsonProperty("inward") String inward, @JsonProperty("outward") String outward) {
        this.inward = inward;
        this.outward = outward;
    }

    public String getInward() {
        return inward;
    }

    public String getOutward() {
        return outward;
    }
}
