package geb.issues.ratpack;

import ratpack.exec.ExecControl;
import ratpack.exec.Promise;
import ratpack.func.Function;
import ratpack.test.exec.ExecHarness;

import javax.inject.Inject;

import static ratpack.util.Exceptions.uncheck;

public class ExceptionUncheckingExecHarness {
    private final ExecHarness execHarness;

    @Inject
    public ExceptionUncheckingExecHarness(ExecHarness execHarness) {
        this.execHarness = execHarness;
    }

    public <T> T yield(Function<ExecControl, Promise<T>> func) {
        try {
            return execHarness.yield(func).getValueOrThrow();
        } catch (Exception e) {
            throw uncheck(e);
        }
    }
}
