package geb.issues

import co.freeside.betamax.Betamax
import co.freeside.betamax.Recorder
import org.junit.Rule
import ratpack.http.client.HttpClient
import ratpack.test.exec.ExecHarness
import spock.guice.UseModules
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

@UseModules(TestModule)
class VersionsSpec extends Specification {

    @Inject
    HttpClient httpClient

    @Inject
    @Shared
    @AutoCleanup
    ExecHarness harness

    @Rule
    Recorder recorder

    @Betamax(tape = "versions")
    def "can retrieve versions"() {
        expect:
        harness.yield { httpClient.get(new URI("http://jira.codehaus.org/rest/api/2/project/GEB/versions")) }.valueOrThrow.statusCode == 200
    }
}
