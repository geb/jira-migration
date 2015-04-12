package geb.issues.github;

import com.fasterxml.jackson.core.type.TypeReference;
import geb.issues.github.data.Import;
import geb.issues.github.data.ImportStatus;
import geb.issues.ratpack.ExceptionUncheckingExecHarness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class GithubIssuesService {
    private final static Logger LOGGER = LoggerFactory.getLogger(GithubIssuesService.class);
    private static final TypeReference<ImportStatus> IMPORT_STATUS_TYPE = new TypeReference<ImportStatus>() {
    };

    private final GithubApiClient client;
    private final ExceptionUncheckingExecHarness execHarness;

    @Inject
    public GithubIssuesService(GithubApiClient client, ExceptionUncheckingExecHarness execHarness) {
        this.client = client;
        this.execHarness = execHarness;
    }

    public ImportStatus importIssue(Import issue) {
        return execHarness.yield(execHarness -> client
                        .post(url -> url.segment("import").segment("issues"), issue, IMPORT_STATUS_TYPE)
                        .onYield(() -> LOGGER.info("Issue '{}' imported into github", issue.getIssue().getTitle()))
        );
    }

    public ImportStatus importStatus(int id) {
        return execHarness.yield(execHarness -> client
                        .get(url -> url.segment("import").segment("issues").segment("%d", id), IMPORT_STATUS_TYPE)
                        .onYield(() -> LOGGER.info("Checking issue import status"))
        );
    }
}
