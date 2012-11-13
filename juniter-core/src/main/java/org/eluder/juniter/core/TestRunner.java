package org.eluder.juniter.core;

import org.eluder.juniter.core.runner.BasicRunner;
import org.eluder.juniter.core.runner.Runner;
import org.eluder.juniter.core.util.ReflectionUtils;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRunner extends org.junit.runner.Runner implements Filterable {

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
        Class<? extends TestLifeCycle>[] lifeCycles = getLifeCycles(context);
        if (lifeCycles == null) {
            throw new InitializationError("Test life cycles cannot be null");
        } else if (lifeCycles.length > 0) {
            log.debug("Test runner using life cycles of {}", (Object) lifeCycles);
        }
        runner.init(klass, getLifeCycles(context));
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

    protected Runner createRunner(final TestContext context) throws InitializationError {
        if (context == null) {
            return new BasicRunner();
        } else {
            try {
                return ReflectionUtils.instantiate(context.runner());
            } catch (IllegalStateException ex) {
                throw new InitializationError(ex);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected Class<? extends TestLifeCycle>[] getLifeCycles(final TestContext context) {
        if (context == null) {
            return new Class[0];
        } else {
            return context.lifeCycles();
        }
    }
}
