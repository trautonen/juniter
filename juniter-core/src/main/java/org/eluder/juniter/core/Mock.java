package org.eluder.juniter.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eluder.juniter.core.test.MockLifeCycle;

/**
 * Generic mock annotation that must be used with {@link MockLifeCycle}.
 * Any test class field or its super class field can be annotated with
 * {@link Mock}. The mocker implementation decides how the mock the field.
 * <p>
 * Juniter default mocker implementation supports Mockito and EasyMock out of
 * the box. Just provide either one in the classpath and it will be used. In
 * tests the selected mock framework can be used normally to define
 * expectations etc.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Mock {

}
