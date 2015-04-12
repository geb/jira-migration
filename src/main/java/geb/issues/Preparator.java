package geb.issues;

import com.google.inject.Guice;
import com.google.inject.Injector;
import geb.issues.github.LabelsService;
import geb.issues.github.MilestonesService;
import geb.issues.github.data.Label;
import geb.issues.github.data.Milestone;
import geb.issues.jira.VersionsService;
import geb.issues.jira.data.IssueResolution;
import geb.issues.jira.data.IssueType;
import geb.issues.jira.data.Version;
import ratpack.test.exec.ExecHarness;

import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

public class Preparator {

    private final MilestonesService milestonesService;
    private final VersionsService versionsService;
    private final LabelsService labelsService;

    public Preparator(Injector injector) {
        versionsService = injector.getInstance(VersionsService.class);
        milestonesService = injector.getInstance(MilestonesService.class);
        labelsService = injector.getInstance(LabelsService.class);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new IssuesModule(), new CredentialsModule(args[0], args[1]));
        try {
            new Preparator(injector).prepare();
        } finally {
            injector.getInstance(ExecHarness.class).close();
        }
    }

    private void prepare() {
        addMilestones();
        clearLabels();
        addLabels();
    }

    private void addLabels() {
        concat(of(IssueType.values()), of(IssueResolution.values()))
                .map(item -> item.asLabel())
                .filter(item -> item != null)
                .distinct()
                .forEach(labelsService::create);
    }

    private void clearLabels() {
        labelsService.labels().stream().map(Label::getName).forEach(labelsService::remove);
    }

    private void addMilestones() {
        versionsService.versions().stream().map(Version::getName).map(Milestone::new).forEach(milestonesService::create);
    }
}
