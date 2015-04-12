package geb.issues;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import geb.issues.github.GithubIssuesService;
import geb.issues.github.MilestonesService;
import geb.issues.github.data.GithubComment;
import geb.issues.github.data.GithubIssue;
import geb.issues.github.data.Import;
import geb.issues.github.data.ImportStatus;
import geb.issues.jira.JiraIssuesService;
import geb.issues.jira.data.*;
import ratpack.test.exec.ExecHarness;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class IssueMigrator {

    private final JiraIssuesService jiraIssuesService;
    private final GithubIssuesService githubIssuesService;
    private final MilestonesService milestoneService;

    public IssueMigrator(Injector injector) {
        jiraIssuesService = injector.getInstance(JiraIssuesService.class);
        githubIssuesService = injector.getInstance(GithubIssuesService.class);
        milestoneService = injector.getInstance(MilestonesService.class);
        milestoneService.loadMilestones();
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new IssuesModule(), new CredentialsModule(args[0], args[1]));
        try {
            new IssueMigrator(injector).migrateIssues(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        } finally {
            injector.getInstance(ExecHarness.class).close();
        }
    }

    private void migrateIssues(int firstIssue, int lastIssue) {
        IntStream.rangeClosed(firstIssue, lastIssue).forEach(this::migrateIssue);
    }

    private void migrateIssue(int issueNumber) {
        JiraIssue issue = jiraIssuesService.issue(issueNumber);
        ImportStatus status;
        if (issue.getFields() == null) {
            status = createEmptyIssue();
        } else {
            status = githubIssuesService.importIssue(convert(issue));
        }
        waitForImportToFinish(status);
    }

    private void waitForImportToFinish(ImportStatus status) {
        int wait = 250;
        while (true) {
            if (githubIssuesService.importStatus(status.getId()).getStatus().equals("imported")) {
                break;
            }
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wait *= 2;
        }
    }

    private ImportStatus createEmptyIssue() {
        GithubIssue issue = new GithubIssue("N/A", "Deleted", null, Collections.emptyList(), null, null, true);
        return githubIssuesService.importIssue(new Import(issue, Collections.emptyList()));
    }

    private Import convert(JiraIssue issue) {
        IssueFields fields = issue.getFields();
        Assignee assignee = fields.getAssignee();
        String gitHubUserName = assignee != null ? assignee.getGitHubUserName() : null;
        String fixVersion = fields.getFixVersion();
        Integer milestone = fixVersion != null ? milestoneService.number(fixVersion) : null;

        GithubIssue converted = new GithubIssue(
                fields.getSummary(),
                issueBody(fields),
                gitHubUserName,
                fields.getLabels(),
                milestone,
                fields.getCreated(),
                issue.getFields().getResolution() != null
        );

        return new Import(converted, getComments(issue));
    }

    private String issueBody(IssueFields fields) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(stream);
        printStream.print("Originally created by ");
        printStream.print(fields.getReporter());
        printStream.print(".");
        if (fields.getDescription() != null) {
            printStream.println();
            printStream.println();
            printStream.print(formatCodeBlocks(fields.getDescription()));
        }
        return stream.toString();
    }

    private List<GithubComment> getComments(JiraIssue issue) {
        List<GithubComment> comments = Lists.newArrayList();
        for (IssueLink issueLink : issue.getFields().getIssueLinks()) {
            comments.add(new GithubComment(issue.getFields().getCreated(), "This issue " + issueLink.getRelation() + " #" + issueLink.getIssue()));
        }
        for (JiraComment comment : issue.getFields().getComments()) {
            comments.add(new GithubComment(comment.getCreated(), commentBody(comment)));
        }
        if (issue.getFields().getResolution() != null) {
            comments.add(new GithubComment(issue.getFields().getResolved(), "Resolved"));
        }
        return comments;
    }

    private String commentBody(JiraComment comment) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(stream);
        printStream.print("Originally posted by ");
        printStream.print(comment.getAuthor());
        printStream.println(".");
        printStream.println();
        printStream.print(formatCodeBlocks(comment.getBody()));
        return stream.toString();
    }

    private String formatCodeBlocks(String text) {
        return text.replaceAll("\\{code(:[^}]+)?\\}", "```").replaceAll("\\{\\{", "`").replaceAll("\\}\\}", "`");
    }
}
