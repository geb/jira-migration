package geb.issues.jira.data;

import geb.issues.github.data.AsLabel;
import geb.issues.github.data.Label;

public enum IssueType implements AsLabel {
    BUG("Bug", "fc2929"),
    EPIC("Story", "207de5"),
    IMPROVEMENT("Improvement", "5319e7"),
    NEW_FEATURE("New feature", "009800"),
    STORY("Story", "207de5"),
    TASK("Task", "cc317c"),
    WISH("New feature", "009800");

    private final Label label;

    IssueType(String name, String color) {
        label = new Label(name, color);
    }

    public Label asLabel() {
        return label;
    }
}
