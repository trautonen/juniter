package org.eluder.testutils.core.invoker;

import org.eluder.testutils.core.TestLifeCycle;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

public class TestLifeCycleInvoker extends AbstractLifeCycleInvoker {

    private final TestLifeCycle testLifeCycle;

    public TestLifeCycleInvoker(final Statement base, final TestClass testClass, final FrameworkMethod method, final Object target, final TestLifeCycle testLifeCycle) {
        super(base, testClass, method, target);
        this.testLifeCycle = testLifeCycle;
    }

    @Override
    public void evaluate() throws Throwable {
        testLifeCycle.onBeforeTest(getTestClass(), getMethod(), getTarget());
        try {
            getBase().evaluate();
            testLifeCycle.onSuccess(getTestClass(), getMethod(), getTarget());
        } catch (AssumptionViolatedException ex) {
            throw ex;
        } catch (Throwable th) {
            testLifeCycle.onFailure(getTestClass(), getMethod(), getTarget());
            throw th;
        } finally {
            testLifeCycle.onAfterTest(getTestClass(), getMethod(), getTarget());
        }
    }

}
