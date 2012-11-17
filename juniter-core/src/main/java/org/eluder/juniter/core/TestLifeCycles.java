package org.eluder.juniter.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eluder.juniter.core.runner.BasicRunner;
import org.eluder.juniter.core.runner.TestLifeCycleBlockRunner;

/**
 * Annotation that defines test life cycles to be executed on test run.
 * This annotation can be set on the test method or the test class or any
 * super class of the test class.
 * <p>
 * The default order of life cycle initialization with {@link TestLifeCycles}
 * is as follows:
 * <ol>
 * <li>test method
 * <li>test class
 * <li>super class of test class
 * <li>super class of super class of test class
 * <li>...
 * </ol>
 * The default order of life cycle initialization can be changed by overriding
 * {@link TestLifeCycleBlockRunner#getTestLifeCycles} or providing custom
 * block runner at {@link BasicRunner#createDelegate}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface TestLifeCycles {

    /**
     * Array of test life cycles that will be executed in the array order.
     */
    Class<? extends TestLifeCycle>[] value() default {};

}
