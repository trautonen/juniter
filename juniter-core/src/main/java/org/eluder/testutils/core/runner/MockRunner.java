package org.eluder.testutils.core.runner;

import java.util.ArrayList;
import java.util.List;

import org.eluder.testutils.core.TestLifeCycle;
import org.eluder.testutils.core.test.MockLifeCycle;
import org.junit.runners.model.InitializationError;

public class MockRunner extends BasicRunner {

    @Override
    protected TestLifeCycleBlockRunner createDelegate(final Class<?> testClass, final List<Class<? extends TestLifeCycle>> testLifeCycles) throws InitializationError {
        final Class<? extends TestLifeCycle> mockLifeCycle = getMockLifeCycle();
        return new TestLifeCycleBlockRunner(testClass, testLifeCycles) {
            @Override
            protected List<Class<? extends TestLifeCycle>> getTestLifeCycles() {
                List<Class<? extends TestLifeCycle>> testLifeCycles = new ArrayList<Class<? extends TestLifeCycle>>();
                testLifeCycles.add(mockLifeCycle);
                testLifeCycles.addAll(super.getTestLifeCycles());
                return testLifeCycles;
            }
        };
    }

    protected Class<? extends TestLifeCycle> getMockLifeCycle() {
        return MockLifeCycle.class;
    }
}
