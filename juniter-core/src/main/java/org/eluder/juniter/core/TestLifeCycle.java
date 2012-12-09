package org.eluder.juniter.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 * <code>TestLifeCycle</code> is hooked up to the test life cycle. Life cycles are executed in the
 * order they are defined in {@link TestLifeCycles} annotation.
 * <p>
 * The full test life cycle with 2 test rules is as follows:
 * <ol>
 * <li>All {@link TestRule}s</li>
 * <li>1st test life cycle {@link #onBeforeBefore(TestClass, FrameworkMethod, Object)}</li>
 * <li>2nd test life cycle {@link #onBeforeBefore(TestClass, FrameworkMethod, Object)}</li>
 * <li>All {@link Before} annotated methods</li>
 * <li>1st test life cycle {@link #onBeforeTest(TestClass, FrameworkMethod, Object)}</li>
 * <li>2nd test life cycle {@link #onBeforeTest(TestClass, FrameworkMethod, Object)}</li>
 * <li>the actual test method annotated with {@link Test}</li>
 * <li>1st test life cycle {@link #onSuccess(TestClass, FrameworkMethod, Object)}, if the test succeeds</li>
 * <li>2nd test life cycle {@link #onSuccess(TestClass, FrameworkMethod, Object)}, if the test succeeds</li>
 * <li>1st test life cycle {@link #onFailure(TestClass, FrameworkMethod, Object)}, if the test fails</li>
 * <li>2nd test life cycle {@link #onFailure(TestClass, FrameworkMethod, Object)}, if the test fails</li>
 * <li>2nd test life cycle {@link #onAfterTest(TestClass, FrameworkMethod, Object)}</li>
 * <li>1st test life cycle {@link #onAfterTest(TestClass, FrameworkMethod, Object)}</li>
 * <li>All {@link After} annotated methods</li>
 * <li>2nd test life cycle {@link #onAfterAfter(TestClass, FrameworkMethod, Object)}</li>
 * <li>1st test life cycle {@link #onAfterAfter(TestClass, FrameworkMethod, Object)}</li>
 * </ol>
 */
public interface TestLifeCycle {

    /**
     * Executed before any {@link Before} annotated method. If execution of this method throws
     * an exception, then the whole life cycle stops. Multiple life cycles are executed in the
     * order they are defined.
     *
     * @param testClass the test class
     * @param method the test method
     * @param target the test instance
     * @throws Throwable if the execution fails
     */
    void onBeforeBefore(TestClass testClass, FrameworkMethod method, Object target) throws Throwable;

    /**
     * Executed before the actual test method annotated with {@link Test}. If execution of this
     * method throws an exception, then the whole life cycle stops. Multiple life cycles are
     * executed in the order they are defined.
     *
     * @param testClass the test class
     * @param method the test method
     * @param target the test instance
     * @throws Throwable if the execution fails
     */
    void onBeforeTest(TestClass testClass, FrameworkMethod method, Object target) throws Throwable;

    /**
     * Executed if the test succeeds. If execution of this method throws an exception, then the
     * rest of the lice cycle is not executed. Multiple life cycles are executed in the order they
     * are defined.
     *
     * @param testClass the test class
     * @param method the test method
     * @param target the test instance
     * @throws Throwable if the execution fails
     */
    void onSuccess(TestClass testClass, FrameworkMethod method, Object target) throws Throwable;

    /**
     * Executed if the test fails. If exectuon if this method throws an exception, then the rest
     * of the life cycle is not executed. Multiple life cycles are exectued in the order they are
     * defined.
     *
     * @param testClass the test class
     * @param method the test method
     * @param target the test instance
     * @throws Throwable if the execution fails
     */
    void onFailure(TestClass testClass, FrameworkMethod method, Object target) throws Throwable;

    /**
     * Executed after the actual test method annotated with {@link Test}. If execution this method
     * throws an exception, then the rest of the life cycle is not executed. Multiple life cycles
     * are executed in the reverse order they are defined.
     *
     * @param testClass the test class
     * @param method the test method
     * @param target the test instance
     * @throws Throwable if the execution fails
     */
    void onAfterTest(TestClass testClass, FrameworkMethod method, Object target) throws Throwable;

    /**
     * Executed after any {@link After} annotated method. All these methods are guaranteed to run
     * even if there are exceptions thrown in the test life cycle. Exceptions are collected and
     * thrown after all methods are run. This allows proper clean up of resources. Multiple life
     * cycles are executed in reverse order they are defined.
     *
     * @param testClass the test class
     * @param method the test method
     * @param target the test instance
     * @throws Throwable if the execution fails
     */
    void onAfterAfter(TestClass testClass, FrameworkMethod method, Object target) throws Throwable;

}
