package geb.issues.jira

import geb.issues.IssuesModule
import geb.issues.jira.data.IssueResolution
import ratpack.test.exec.ExecHarness
import spock.guice.UseModules
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject
import java.time.LocalDate
import java.time.ZoneOffset

import static geb.issues.jira.data.Assignee.MARCIN_ERDMANN
import static geb.issues.jira.data.IssueType.BUG

@UseModules([IssuesModule])
class IssuesServiceSpec extends Specification {

    @Inject
    JiraIssuesService issuesService

    @Inject
    @Shared
    @AutoCleanup
    ExecHarness harness

    def "can retrieve issues"() {
        when:
        def issue = issuesService.issue(250)
        def fields = issue.fields

        then:
        fields.summary == "to: content option and click(Class<? extends Page>) should verify page after clicking"
        fields.description == "As to() now verifies page at checker, click() and to: should do the same to stay consistent."
        fields.resolution == IssueResolution.FIXED
        fields.fixVersion == "0.9.0"
        fields.reporter == "Marcin Erdmann"
        fields.created.withOffsetSameInstant(ZoneOffset.UTC).toLocalDate() == LocalDate.of(2013, 3, 2)
        fields.assignee == MARCIN_ERDMANN
        fields.issueType == BUG

        def comment = fields.comments.first()
        comment.created.withOffsetSameInstant(ZoneOffset.UTC).toLocalDate() == LocalDate.of(2013, 3, 2)
        comment.body == "Resolved in https://github.com/geb/geb/commit/b3a67f79010f116d5cfd3a96a60ff952769913ba"
        comment.author == "Marcin Erdmann"
    }

    def "can retrieve issues with links"() {
        when:
        def issue = issuesService.issue(10)
        def fields = issue.fields

        then:
        fields.issueLinks*.relation == ["is superceded by"]
        fields.issueLinks*.issue == [78]

        when:
        issue = issuesService.issue(11)
        fields = issue.fields

        then:
        fields.issueLinks*.relation == ["depends upon"]
        fields.issueLinks*.issue == [40]
    }
}
