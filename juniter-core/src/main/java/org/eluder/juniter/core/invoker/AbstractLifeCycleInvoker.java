package org.eluder.juniter.core.invoker;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;


public abstract class AbstractLifeCycleInvoker extends Statement {

    private final Statement base;
    private final TestClass testClass;
    private final FrameworkMethod method;
    private final Object target;

    public AbstractLifeCycleInvoker(final Statement base, final TestClass testClass, final FrameworkMethod method, final Object target) {
        this.base = base;
        this.testClass = testClass;
        this.method = method;
        this.target = target;
    }

    public Statement getBase() {
        return base;
    }

    public TestClass getTestClass() {
        return testClass;
    }

    public FrameworkMethod getMethod() {
        return method;
    }

    public Object getTarget() {
        return target;
    }

}
