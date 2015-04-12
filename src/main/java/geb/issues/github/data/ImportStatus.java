package geb.issues.github.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImportStatus {

    private final String status;
    private final Integer id;

    public ImportStatus(@JsonProperty("status") String status, @JsonProperty("id") Integer id) {
        this.status = status;
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }
}
