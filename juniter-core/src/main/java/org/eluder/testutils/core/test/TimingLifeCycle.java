package org.eluder.testutils.core.test;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimingLifeCycle extends BaseLifeCycle {

    private static final Logger log = LoggerFactory.getLogger(TimingLifeCycle.class);

    private long started;

    @Override
    public void onBeforeTest(final TestClass testClass, final FrameworkMethod method, final Object target) {
        this.started = System.currentTimeMillis();
    }

    @Override
    public void onAfterTest(final TestClass testClass, final FrameworkMethod method, final Object target) {
        long duration = System.currentTimeMillis() - started;
        handleDuration(testClass, method, duration);
    }

    protected void handleDuration(final TestClass testClass, final FrameworkMethod method, final long duration) {
        log.info("Test '{}.{}' took {}ms", new Object[] { testClass.getName(), method.getName(), duration });
    }
}
