package org.eluder.juniter.guice;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configures how the Guice injector context is created.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface GuiceContext {

    /**
     * Should all provided mocks be bound to the injector context.
     */
    boolean bindMocks() default true;

    /**
     * Should all non primitive and non mock fields be automatically instantiated and bound to the
     * injector context.
     */
    boolean autoContext() default true;
}
