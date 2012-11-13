package org.eluder.testutils.core.runner;

import java.util.Collections;
import java.util.List;

import org.eluder.testutils.core.TestLifeCycle;
import org.eluder.testutils.core.invoker.AfterLifeCycleInvoker;
import org.eluder.testutils.core.invoker.BeforeLifeCycleInvoker;
import org.eluder.testutils.core.invoker.MagicFrameWorkMethod;
import org.eluder.testutils.core.invoker.TestLifeCycleInvoker;
import org.eluder.testutils.core.util.ReflectionUtils;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class TestLifeCycleBlockRunner extends BlockJUnit4ClassRunner {

    private final List<Class<? extends TestLifeCycle>> testLifeCycles;

    public TestLifeCycleBlockRunner(final Class<?> klass, final List<Class<? extends TestLifeCycle>> testLifeCycles) throws InitializationError {
        super(klass);
        this.testLifeCycles = testLifeCycles;
    }

    protected List<Class<? extends TestLifeCycle>> getTestLifeCycles() {
        return testLifeCycles;
    }

    @Override
    protected Statement methodBlock(final FrameworkMethod method) {
        List<TestLifeCycle> testLifeCycles = initTestLifeCycles(getTestLifeCycles());
        return super.methodBlock(new MagicFrameWorkMethod(method, testLifeCycles));
    }

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
        MagicFrameWorkMethod magicMethod = (MagicFrameWorkMethod) method;
        Statement invoker = super.methodInvoker(method, test);
        for (TestLifeCycle lifeCycle : Lists.reverse(magicMethod.getTestLifeCycles())) {
            invoker = new TestLifeCycleInvoker(invoker, getTestClass(), method, test, lifeCycle);
        }
        return invoker;
    }

    @Override
    @Deprecated
    protected Statement withBefores(final FrameworkMethod method, final Object target, final Statement statement) {
        MagicFrameWorkMethod magicMethod = (MagicFrameWorkMethod) method;
        Statement invoker = super.withBefores(method, target, statement);
        return new BeforeLifeCycleInvoker(invoker, getTestClass(), method, target, magicMethod.getTestLifeCycles());
    }

    @Override
    @Deprecated
    protected Statement withAfters(final FrameworkMethod method, final Object target, final Statement statement) {
        MagicFrameWorkMethod magicMethod = (MagicFrameWorkMethod) method;
        Statement invoker = super.withAfters(method, target, statement);
        return new AfterLifeCycleInvoker(invoker, getTestClass(), method, target, magicMethod.getTestLifeCycles());
    }

    private List<TestLifeCycle> initTestLifeCycles(final List<Class<? extends TestLifeCycle>> testLifeCycles) {
        if (testLifeCycles == null || testLifeCycles.isEmpty()) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(Lists.transform(testLifeCycles, new Function<Class<? extends TestLifeCycle>, TestLifeCycle>() {
                @Override
                public TestLifeCycle apply(final Class<? extends TestLifeCycle> input) {
                    return ReflectionUtils.instantiate(input);
                }
            }));
        }
    }
}
