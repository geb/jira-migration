package geb.issues.github;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import geb.issues.ratpack.ExceptionUncheckingExecHarness;
import geb.issues.github.data.Milestone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class MilestonesService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MilestonesService.class);

    private final ExceptionUncheckingExecHarness execHarness;
    private final GithubApiClient client;
    private ImmutableMap<String, Integer> milestones;

    @Inject
    public MilestonesService(ExceptionUncheckingExecHarness execHarness, GithubApiClient client) {
        this.execHarness = execHarness;
        this.client = client;
    }

    public void create(Milestone milestone) {
        execHarness.yield(execControl -> client.post(url -> url.segment("milestones"), milestone)
                        .onYield(() -> LOGGER.info("Milestone {} created", milestone.getTitle()))
        );
    }

    public List<Milestone> milestones() {
        return execHarness.yield(execControl -> client.get(url -> url.segment("milestones"), new TypeReference<List<Milestone>>() {
        }).onYield(() -> LOGGER.info("Milestones retrieved")));
    }

    public void loadMilestones() {
        ImmutableMap.Builder<String, Integer> milestones = ImmutableMap.builder();
        for (Milestone milestone : milestones()) {
            milestones.put(milestone.getTitle(), milestone.getNumber());
        }
        this.milestones = milestones.build();
    }

    public int number(String title) {
        return milestones.get(title);
    }
}
