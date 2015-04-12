package geb.issues.jira.data.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import geb.issues.jira.data.Assignee;

import java.io.IOException;

public class AssigneeDeserializer extends JsonDeserializer<Assignee> {
    @Override
    public Assignee deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        return Assignee.valueOf(node.get("displayName").asText().replaceAll(" ", "_").toUpperCase());
    }
}
