package org.eluder.juniter.core.test;

import org.eluder.juniter.core.TestLifeCycle;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

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
