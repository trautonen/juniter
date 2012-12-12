package org.eluder.juniter.core.runner;

/*
 * #[license]
 * juniter-core
 * %%
 * Copyright (C) 2012 Tapio Rautonen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * %[license]
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

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

/**
 * The real test runner that binds juniter {@link TestLifeCycle}s to JUnit tests. Uses
 * {@link LifeCycleHoldingMethod} to wrap the framework method and provide a context for test
 * life cycles. All convenient hooks are provided with {@link LifeCycleHoldingMethod} so that
 * extending the juniter framework is as easy as possible.
 */
public class TestLifeCycleBlockRunner extends BlockJUnit4ClassRunner {

    public TestLifeCycleBlockRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    /**
     * Returns the test life cycles. Test life cycles are collected starting from the test method
     * and then the method defining class and after that all super classes of the test class.
     * Subclasses can change the order of test life cycles or edit the default list.
     *
     * @param method the test method
     */
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
        ListIterator<TestLifeCycle> iter = method.getTestLifeCycles().listIterator(method.getTestLifeCycles().size());
        while (iter.hasPrevious()) {
            TestLifeCycle lifeCycle = iter.previous();
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
            List<TestLifeCycle> instances = new ArrayList<TestLifeCycle>(testLifeCycles.size());
            for (Class<? extends TestLifeCycle> testLifeCycle : testLifeCycles) {
                instances.add(ReflectionUtils.instantiate(testLifeCycle));
            }
            return Collections.unmodifiableList(instances);
        }
    }
}
