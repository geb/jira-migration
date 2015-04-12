package geb.issues.github.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Label {
    private final String name;
    private final String color;

    public Label(@JsonProperty("name") String name, @JsonProperty("color") String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int hashCode() {
        return Objects.hash(name, color);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Label)) {
            return false;
        }

        Label other = (Label) obj;
        return Objects.equals(name, other.name) &&
                Objects.equals(color, other.color);
    }
}
