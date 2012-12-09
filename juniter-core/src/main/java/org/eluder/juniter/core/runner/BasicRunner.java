package org.eluder.juniter.core.runner;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BasicRunner implements Runner {

    private static final Logger log = LoggerFactory.getLogger(BasicRunner.class);

    private TestLifeCycleBlockRunner delegate;

    @Override
    public final void init(final Class<?> testClass) throws InitializationError {
        delegate = createDelegate(testClass);
        if (delegate == null) {
            throw new InitializationError("JUnit block runner delegate not created properly");
        } else {
            log.debug("{} set as JUnit block runner delegate", delegate.getClass().getName());
        }
    }

    @Override
    public final Description getDescription() {
        return delegate.getDescription();
    }

    @Override
    public final void run(final RunNotifier notifier) {
        delegate.run(notifier);
    }

    @Override
    public final void filter(final Filter filter) throws NoTestsRemainException {
        delegate.filter(filter);
    }

    @Override
    public void sort(final Sorter sorter) {
        delegate.sort(sorter);
    }

    protected TestLifeCycleBlockRunner createDelegate(final Class<?> testClass) throws InitializationError {
        return new TestLifeCycleBlockRunner(testClass);
    }
}
