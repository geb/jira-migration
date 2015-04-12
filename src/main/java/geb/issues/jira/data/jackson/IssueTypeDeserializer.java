package geb.issues.jira.data.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import geb.issues.jira.data.IssueType;

import java.io.IOException;

public class IssueTypeDeserializer extends JsonDeserializer<IssueType> {
    @Override
    public IssueType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String name = node.get("name").asText();
        return IssueType.valueOf(name.replaceAll(" ", "_").toUpperCase());
    }
}
