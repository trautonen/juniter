package org.eluder.juniter.core;

import org.eluder.juniter.core.runner.BasicRunner;
import org.eluder.juniter.core.runner.Runner;
import org.eluder.juniter.core.util.ReflectionUtils;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRunner extends org.junit.runner.Runner implements Filterable, Sortable {

    private static final Logger log = LoggerFactory.getLogger(TestRunner.class);

    private final Runner runner;

    public TestRunner(final Class<?> klass) throws InitializationError {
        TestContext context = klass.getAnnotation(TestContext.class);
        runner = createRunner(context);
        if (runner == null) {
            throw new InitializationError("Runner not created properly");
        } else {
            log.debug("Test runner initialized with {}", runner.getClass().getName());
        }
        runner.init(klass);
    }

    @Override
    public final Description getDescription() {
        return runner.getDescription();
    }

    @Override
    public final void run(final RunNotifier notifier) {
        runner.run(notifier);
    }

    @Override
    public final void filter(final Filter filter) throws NoTestsRemainException {
        runner.filter(filter);
    }

    @Override
    public void sort(final Sorter sorter) {
        runner.sort(sorter);
    }

    protected Runner createRunner(final TestContext context) throws InitializationError {
        if (context == null) {
            return new BasicRunner();
        } else {
            return ReflectionUtils.instantiate(context.runner());
        }
    }
}
