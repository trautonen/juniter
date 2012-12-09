package org.eluder.juniter.core.test;

import org.eluder.juniter.core.TestLifeCycle;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 * Simple base class for test life cycles. Provides convinient empty methods so that only those
 * that are relevant to the implementation can be implemented.
 */
public class BaseLifeCycle implements TestLifeCycle {

    @Override
    public void onBeforeBefore(final TestClass testClass, final FrameworkMethod method, final Object target) {
        // noop
    }

    @Override
    public void onBeforeTest(final TestClass testClass, final FrameworkMethod method, final Object target) {
        // noop
    }

    @Override
    public void onSuccess(final TestClass testClass, final FrameworkMethod method, final Object target) {
        // noop
    }

    @Override
    public void onFailure(final TestClass testClass, final FrameworkMethod method, final Object target) {
        // noop
    }

    @Override
    public void onAfterTest(final TestClass testClass, final FrameworkMethod method, final Object target) {
        // noop
    }

    @Override
    public void onAfterAfter(final TestClass testClass, final FrameworkMethod method, final Object target) {
        // noop
    }
}
