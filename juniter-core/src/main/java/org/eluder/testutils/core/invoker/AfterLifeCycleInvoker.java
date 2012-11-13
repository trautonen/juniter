package org.eluder.testutils.core.invoker;

import java.util.ArrayList;
import java.util.List;

import org.eluder.testutils.core.TestLifeCycle;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import com.google.common.collect.Lists;

public class AfterLifeCycleInvoker extends AbstractLifeCycleInvoker {

    private final List<TestLifeCycle> testLifeCycles;

    public AfterLifeCycleInvoker(final Statement base, final TestClass testClass, final FrameworkMethod method, final Object target, final List<TestLifeCycle> testLifeCycles) {
        super(base, testClass, method, target);
        this.testLifeCycles = testLifeCycles;
    }

    @Override
    public void evaluate() throws Throwable {
        List<Throwable> errors = new ArrayList<Throwable>();
        try {
            getBase().evaluate();
        } catch (Throwable th) {
            errors.add(th);
        } finally {
            for (TestLifeCycle testLifeCycle : Lists.reverse(testLifeCycles)) {
                try {
                    testLifeCycle.onAfter(getTestClass(), getMethod(), getTarget());
                } catch (Throwable th) {
                    errors.add(th);
                }
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }
}
