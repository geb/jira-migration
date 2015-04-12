package geb.issues.jira;

import com.fasterxml.jackson.databind.ObjectMapper;
import geb.issues.ratpack.ExceptionUncheckingExecHarness;
import geb.issues.jira.data.JiraIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.http.client.HttpClient;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

import static ratpack.util.Exceptions.uncheck;

public class JiraIssuesService {
    private final static Logger LOGGER = LoggerFactory.getLogger(JiraIssuesService.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ExceptionUncheckingExecHarness execHarness;

    private final static String ISSUE_URL_PATTERN = "http://jira.codehaus.org/rest/api/2/issue/GEB-%d";

    @Inject
    public JiraIssuesService(HttpClient httpClient, ObjectMapper objectMapper, ExceptionUncheckingExecHarness execHarness) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.execHarness = execHarness;
    }

    public JiraIssue issue(int number) {
        return execHarness.yield(execHarness -> httpClient.get(issueUri(number))
                .onYield(() -> LOGGER.info("Issue GEB-{} retrieved from JIRA", number))
                .map(receivedResponse -> objectMapper.readValue(receivedResponse.getBody().getInputStream(), JiraIssue.class)));
    }

    private URI issueUri(int number) {
        URI issueUri = null;
        try {
            issueUri = new URI(String.format(ISSUE_URL_PATTERN, number));
        } catch (URISyntaxException e) {
            uncheck(e);
        }
        return issueUri;
    }
}
