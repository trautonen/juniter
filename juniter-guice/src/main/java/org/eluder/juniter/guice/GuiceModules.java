package org.eluder.juniter.guice;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.Module;

/**
 * Provides the Guice modules. All modules must have zero arg constructor so they can be
 * automatically instantiated.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface GuiceModules {

    /**
     * Module types.
     */
    Class<? extends Module>[] value() default {};

}
