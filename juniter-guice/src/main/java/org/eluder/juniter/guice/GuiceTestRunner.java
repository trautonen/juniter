package org.eluder.juniter.guice;

import org.eluder.juniter.core.TestContext;
import org.eluder.juniter.core.TestLifeCycles;
import org.eluder.juniter.core.TestRunner;
import org.eluder.juniter.core.runner.Runner;
import org.eluder.juniter.guice.runner.GuiceRunner;
import org.junit.runners.model.InitializationError;

/**
 * Convenient Guice test runner that uses {@link GuiceRunner} as the runner
 * implementation. Provides automatic mocking and context initialization
 * without any other configuration. Also supports additional life cycles with
 * {@link TestLifeCycles} annotation.
 */
public class GuiceTestRunner extends TestRunner {

    public GuiceTestRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Runner createRunner(final TestContext context) throws InitializationError {
        return new GuiceRunner();
    }
}
