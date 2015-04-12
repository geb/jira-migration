package geb.issues.jira;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import geb.issues.ratpack.ExceptionUncheckingExecHarness;
import geb.issues.jira.data.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.http.client.HttpClient;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static ratpack.util.Exceptions.uncheck;

public class VersionsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(VersionsService.class);

    static {
        URI endpoint = null;
        try {
            endpoint = new URI("http://jira.codehaus.org/rest/api/2/project/GEB/versions");
        } catch (URISyntaxException e) {
            uncheck(e);
        }
        VERSIONS_ENDPOINT = endpoint;
    }

    private final static URI VERSIONS_ENDPOINT;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ExceptionUncheckingExecHarness execHarness;

    @Inject
    public VersionsService(HttpClient httpClient, ObjectMapper objectMapper, ExceptionUncheckingExecHarness execHarness) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.execHarness = execHarness;
    }

    public List<Version> versions() {
        return execHarness.yield(execHarness -> httpClient.get(VERSIONS_ENDPOINT)
                .onYield(() -> LOGGER.info("List of versions retrieved form JIRA"))
                .map(receivedResponse -> {
                    TypeReference<List<Version>> versionListType = new TypeReference<List<Version>>() {
                    };
                    return objectMapper.readValue(receivedResponse.getBody().getInputStream(), versionListType);
                }));
    }
}
