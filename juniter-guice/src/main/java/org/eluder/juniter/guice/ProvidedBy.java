package org.eluder.juniter.guice;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.Provider;

/**
 * Provided by annotation for field values. Can be used together with auto context
 * generation for Guice test life cycle.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ProvidedBy {

    /**
     * Type that is provider for the interface or abstract type.
     */
    Class<? extends Provider<?>> value ();
    
}
