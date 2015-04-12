package geb.issues.jira

import geb.issues.IssuesModule
import geb.issues.jira.data.Version
import ratpack.test.exec.ExecHarness
import spock.guice.UseModules
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject
import java.time.LocalDate

@UseModules([IssuesModule])
class VersionsServiceSpec extends Specification {

    @Inject
    VersionsService versionsService

    @Inject
    @Shared
    @AutoCleanup
    ExecHarness harness

    def "can retrieve versions"() {
        when:
        def versions = versionsService.versions()

        then:
        new Version("0.4", true, LocalDate.of(2010, 8, 27)) in versions
        new Version("0.10.1", false, null) in versions
    }
}
