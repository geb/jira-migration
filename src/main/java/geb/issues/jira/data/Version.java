package geb.issues.jira.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

public class Version {
    private final String name;
    private final boolean released;
    private final LocalDate releaseDate;

    public Version(
            @JsonProperty("name") String name,
            @JsonProperty("released") boolean released,
            @JsonProperty("releaseDate") LocalDate releaseDate
    ) {
        this.name = name;
        this.released = released;
        this.releaseDate = releaseDate;
    }

    public String getName() {
        return name;
    }

    public boolean isReleased() {
        return released;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, released, releaseDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Version)) {
            return false;
        }

        Version other = (Version) obj;
        return Objects.equals(name, other.name) &&
                Objects.equals(released, other.released) &&
                Objects.equals(releaseDate, other.releaseDate);
    }
}
