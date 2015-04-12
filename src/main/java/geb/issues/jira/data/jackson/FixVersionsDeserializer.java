package geb.issues.jira.data.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Iterables;

import java.io.IOException;

public class FixVersionsDeserializer extends JsonDeserializer<String> {

    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonNode first = Iterables.getFirst(node, null);
        if (first == null) {
            return null;
        } else {
            return first.get("name").asText();
        }
    }
}
