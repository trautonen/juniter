package org.eluder.juniter.core.runner;

import java.util.ArrayList;
import java.util.List;

import org.eluder.juniter.core.TestLifeCycle;
import org.eluder.juniter.core.test.MockLifeCycle;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class MockRunner extends BasicRunner {

    @Override
    protected TestLifeCycleBlockRunner createDelegate(final Class<?> testClass) throws InitializationError {
        final Class<? extends TestLifeCycle> mockLifeCycle = getMockLifeCycle();
        return new TestLifeCycleBlockRunner(testClass) {
            @Override
            protected List<Class<? extends TestLifeCycle>> getTestLifeCycles(final FrameworkMethod method) {
                List<Class<? extends TestLifeCycle>> testLifeCycles = new ArrayList<Class<? extends TestLifeCycle>>();
                testLifeCycles.add(mockLifeCycle);
                testLifeCycles.addAll(super.getTestLifeCycles(method));
                return testLifeCycles;
            }
        };
    }

    protected Class<? extends TestLifeCycle> getMockLifeCycle() {
        return MockLifeCycle.class;
    }
}
