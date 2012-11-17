package org.eluder.juniter.core;

import org.eluder.juniter.core.runner.MockRunner;
import org.eluder.juniter.core.runner.Runner;
import org.junit.runners.model.InitializationError;

/**
 * Convenient mocking test runner that uses {@link MockRunner} as the runner
 * implementation. Provides automatic mocking without any other configuration.
 * Also supports additional life cycles with {@link TestLifeCycles} annotation.
 */
public class MockTestRunner extends TestRunner {

    public MockTestRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Runner createRunner(final TestContext context) throws InitializationError {
        return new MockRunner();
    }
}
