package org.eluder.testutils.guice;

import org.eluder.testutils.core.TestContext;
import org.eluder.testutils.core.TestRunner;
import org.eluder.testutils.core.runner.Runner;
import org.eluder.testutils.guice.runner.GuiceRunner;
import org.junit.runners.model.InitializationError;

public class GuiceTestRunner extends TestRunner {

    public GuiceTestRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Runner createRunner(final TestContext context) throws InitializationError {
        return new GuiceRunner();
    }
}
