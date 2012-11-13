package org.eluder.juniter.guice;

import org.eluder.juniter.core.TestContext;
import org.eluder.juniter.core.TestRunner;
import org.eluder.juniter.core.runner.Runner;
import org.eluder.juniter.guice.runner.GuiceRunner;
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
