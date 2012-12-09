package org.eluder.juniter.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eluder.juniter.core.runner.BasicRunner;
import org.eluder.juniter.core.runner.Runner;

/**
 * Test context provides configuration for the juniter test runner. Custom test runner can be
 * configured per test class with this annotation. If there is no annotation available or the
 * annotation does not provide a runner {@link BasicRunner} is used.
 * <p>
 * If runner type is configured with this annotation, it must have default constructor, which will
 * be used when instantiated.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestContext {

    /**
     * Defines the runner type. Defaults to {@link BasicRunner}.
     */
    Class<? extends Runner> runner() default BasicRunner.class;

}
