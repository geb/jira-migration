package geb.issues.jira.data.jackson;

public class DisplayNameDeserializer extends NestedFieldDeserializer {
    public DisplayNameDeserializer() {
        super("displayName");
    }
}
