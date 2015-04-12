package geb.issues.github.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class GithubComment {

    private final OffsetDateTime createdAt;
    private final String body;

    public GithubComment(OffsetDateTime createdAt, String body) {
        this.body = body;
        this.createdAt = createdAt;
    }

    @JsonProperty("created_at")
    @JsonInclude(NON_NULL)
    public String getCreatedAt() {
        return createdAt.toString();
    }

    public String getBody() {
        return body;
    }
}
