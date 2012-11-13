package org.eluder.juniter.core;

import org.eluder.juniter.core.runner.MockRunner;
import org.eluder.juniter.core.runner.Runner;
import org.junit.runners.model.InitializationError;

public class MockTestRunner extends TestRunner {

    public MockTestRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Runner createRunner(final TestContext context) throws InitializationError {
        return new MockRunner();
    }
}
