package geb.issues.github.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Milestone {
    private final String title;
    private final Integer number;

    public Milestone(String title) {
        this(title, null);
    }

    @JsonCreator
    public Milestone(
            @JsonProperty("title") String title,
            @JsonProperty("number") Integer number
    ) {
        this.title = title;
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    @JsonIgnore
    public Integer getNumber() {
        return number;
    }
}
