package org.eluder.juniter.core.invoker;

import java.util.List;

import org.eluder.juniter.core.TestLifeCycle;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

public class BeforeLifeCycleInvoker extends AbstractLifeCycleInvoker {

    private final List<TestLifeCycle> testLifeCycles;

    public BeforeLifeCycleInvoker(final Statement base, final TestClass testClass, final FrameworkMethod method, final Object target, final List<TestLifeCycle> testLifeCycles) {
        super(base, testClass, method, target);
        this.testLifeCycles = testLifeCycles;
    }

    @Override
    public void evaluate() throws Throwable {
        for (TestLifeCycle testLifeCycle : testLifeCycles) {
            testLifeCycle.onBefore(getTestClass(), getMethod(), getTarget());
        }
        getBase().evaluate();
    }
}
