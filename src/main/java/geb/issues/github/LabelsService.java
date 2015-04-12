package geb.issues.github;

import com.fasterxml.jackson.core.type.TypeReference;
import geb.issues.github.data.Label;
import geb.issues.ratpack.ExceptionUncheckingExecHarness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class LabelsService {
    private final static Logger LOGGER = LoggerFactory.getLogger(LabelsService.class);

    private final GithubApiClient client;
    private final ExceptionUncheckingExecHarness execHarness;

    @Inject
    public LabelsService(GithubApiClient client, ExceptionUncheckingExecHarness execHarness) {
        this.client = client;
        this.execHarness = execHarness;
    }

    public List<Label> labels() {
        return execHarness.yield(execHarness -> client
                        .get(url -> url.segment("labels"), new TypeReference<List<Label>>() {
                        })
                        .onYield(() -> LOGGER.info("Labels retrieved from GitHub"))
        );
    }

    public void remove(String name) {
        execHarness.yield(execHarness -> client
                        .delete(url -> url.segment("labels").segment(name))
                        .onYield(() -> LOGGER.info("Label {} removed", name))
        );
    }

    public void create(Label label) {
        execHarness.yield(execHarness -> client
                        .post(url -> url.segment("labels"), label)
                        .onYield(() -> LOGGER.info("Label {} created", label.getName()))
        );
    }
}
