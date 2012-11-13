package org.eluder.juniter.core;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public interface TestLifeCycle {

    void onBefore(TestClass testClass, FrameworkMethod method, Object target) throws Throwable;

    void onBeforeTest(TestClass testClass, FrameworkMethod method, Object target) throws Throwable;

    void onSuccess(TestClass testClass, FrameworkMethod method, Object target) throws Throwable;

    void onFailure(TestClass testClass, FrameworkMethod method, Object target) throws Throwable;

    void onAfterTest(TestClass testClass, FrameworkMethod method, Object target) throws Throwable;

    void onAfter(TestClass testClass, FrameworkMethod method, Object target) throws Throwable;

}
