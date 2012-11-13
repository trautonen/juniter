package org.eluder.testutils.core;

import org.eluder.testutils.core.runner.MockRunner;
import org.eluder.testutils.core.runner.Runner;
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
