package geb.issues.jira.data.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class NestedFieldDeserializer extends JsonDeserializer<String> {

    private final String fieldName;

    public NestedFieldDeserializer(String fieldName) {
        this.fieldName = fieldName;
    }

    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        return node.get(fieldName).asText();
    }
}
