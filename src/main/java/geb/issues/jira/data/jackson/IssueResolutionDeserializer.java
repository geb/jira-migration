package geb.issues.jira.data.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import geb.issues.jira.data.IssueResolution;

import java.io.IOException;

public class IssueResolutionDeserializer extends JsonDeserializer<IssueResolution> {

    public IssueResolution deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String name = node.get("name").asText();
        return IssueResolution.valueOf(name.replaceAll("'", "").replaceAll(" ", "_").toUpperCase());
    }
}
