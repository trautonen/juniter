package org.eluder.testutils.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eluder.testutils.core.runner.BasicRunner;
import org.eluder.testutils.core.runner.Runner;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestContext {

    Class<? extends Runner> runner() default BasicRunner.class;

    Class<? extends TestLifeCycle>[] lifeCycles() default {};
}
