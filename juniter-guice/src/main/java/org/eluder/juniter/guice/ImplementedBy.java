package org.eluder.juniter.guice;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Implemented by annotation for field values. Can be used together with auto context
 * generation for Guice test life cycle.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ImplementedBy {

    /**
     * Type that implements the interface or abstract type.
     */
    Class<?> value ();

}
