package geb.issues.jira.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import geb.issues.jira.data.jackson.CustomOffsetDateTimeDeserializer;
import geb.issues.jira.data.jackson.DisplayNameDeserializer;

import java.time.OffsetDateTime;

public class JiraComment {
    private final OffsetDateTime created;
    private final String body;
    private final String author;

    public JiraComment(
            @JsonProperty("created") @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class) OffsetDateTime created,
            @JsonProperty("body") String body,
            @JsonProperty("author") @JsonDeserialize(using = DisplayNameDeserializer.class) String author) {
        this.created = created;
        this.body = body;
        this.author = author;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }
}
