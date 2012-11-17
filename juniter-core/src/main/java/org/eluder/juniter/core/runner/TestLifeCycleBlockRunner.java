package org.eluder.juniter.core.runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eluder.juniter.core.TestLifeCycle;
import org.eluder.juniter.core.TestLifeCycles;
import org.eluder.juniter.core.invoker.AfterLifeCycleInvoker;
import org.eluder.juniter.core.invoker.BeforeLifeCycleInvoker;
import org.eluder.juniter.core.invoker.LifeCycleHoldingMethod;
import org.eluder.juniter.core.invoker.TestLifeCycleInvoker;
import org.eluder.juniter.core.util.ReflectionUtils;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class TestLifeCycleBlockRunner extends BlockJUnit4ClassRunner {

    public TestLifeCycleBlockRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    protected List<Class<? extends TestLifeCycle>> getTestLifeCycles(final FrameworkMethod method) {
        List<Class<? extends TestLifeCycle>> testLifeCycles = new ArrayList<Class<? extends TestLifeCycle>>();
        TestLifeCycles methodAnnotation = method.getAnnotation(TestLifeCycles.class);
        if (methodAnnotation != null) {
            testLifeCycles.addAll(Arrays.asList(methodAnnotation.value()));
        }
        List<TestLifeCycles> classAnnotations = ReflectionUtils.getAnnotationsRecursive(method.getMethod().getDeclaringClass(), TestLifeCycles.class);
        for (TestLifeCycles classAnnotation : classAnnotations) {
            testLifeCycles.addAll(Arrays.asList(classAnnotation.value()));
        }
        return testLifeCycles;
    }

    @Override
    protected final Statement methodBlock(final FrameworkMethod method) {
        List<TestLifeCycle> testLifeCycles = initTestLifeCycles(getTestLifeCycles(method));
        LifeCycleHoldingMethod lifeCycleHoldingMethod = new LifeCycleHoldingMethod(method, testLifeCycles);
        Statement statement = super.methodBlock(lifeCycleHoldingMethod);
        return methodBlock(lifeCycleHoldingMethod, statement);
    }

    protected Statement methodBlock(final LifeCycleHoldingMethod method, final Statement statement) {
        return statement;
    }

    @Override
    protected final Statement methodInvoker(final FrameworkMethod method, final Object test) {
        return methodInvoker((LifeCycleHoldingMethod) method, test);
    }

    protected Statement methodInvoker(final LifeCycleHoldingMethod method, final Object test) {
        Statement invoker = super.methodInvoker(method, test);
        for (TestLifeCycle lifeCycle : Lists.reverse(method.getTestLifeCycles())) {
            invoker = new TestLifeCycleInvoker(invoker, getTestClass(), method, test, lifeCycle);
        }
        return invoker;
    }

    @Override
    protected final Statement possiblyExpectingExceptions(final FrameworkMethod method, final Object test, final Statement next) {
        return possiblyExpectingExceptions((LifeCycleHoldingMethod) method, test, next);
    }

    @SuppressWarnings("deprecation")
    protected Statement possiblyExpectingExceptions(final LifeCycleHoldingMethod method, final Object target, final Statement statement) {
        return super.possiblyExpectingExceptions(method, target, statement);
    }

    @Override
    protected final Statement withPotentialTimeout(final FrameworkMethod method, final Object test, final Statement next) {
        return withPotentialTimeout((LifeCycleHoldingMethod) method, test, next);
    }

    @SuppressWarnings("deprecation")
    protected Statement withPotentialTimeout(final LifeCycleHoldingMethod method, final Object target, final Statement statement) {
        return super.withPotentialTimeout(method, target, statement);
    }

    @Override
    protected final Statement withBefores(final FrameworkMethod method, final Object target, final Statement statement) {
        return withBefores((LifeCycleHoldingMethod) method, target, statement);
    }

    protected Statement withBefores(final LifeCycleHoldingMethod method, final Object target, final Statement statement) {
        @SuppressWarnings("deprecation")
        Statement invoker = super.withBefores(method, target, statement);
        return new BeforeLifeCycleInvoker(invoker, getTestClass(), method, target, method.getTestLifeCycles());
    }

    @Override
    protected final Statement withAfters(final FrameworkMethod method, final Object target, final Statement statement) {
        return withAfters((LifeCycleHoldingMethod) method, target, statement);
    }

    protected Statement withAfters(final LifeCycleHoldingMethod method, final Object target, final Statement statement) {
        @SuppressWarnings("deprecation")
        Statement invoker = super.withAfters(method, target, statement);
        return new AfterLifeCycleInvoker(invoker, getTestClass(), method, target, method.getTestLifeCycles());
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
