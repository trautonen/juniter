package org.eluder.juniter.guice.module;

/*
 * #[license]
 * juniter-guice
 * %%
 * Copyright (C) 2012 Tapio Rautonen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * %[license]
 */

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eluder.juniter.core.Mock;
import org.eluder.juniter.core.TestLifeCycleException;
import org.eluder.juniter.core.util.ReflectionUtils;
import org.eluder.juniter.guice.ImplementedBy;
import org.junit.runners.model.TestClass;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;

public class AutoContextModule extends AbstractModule {

    private final List<Field> fields;
    private final Object target;

    public AutoContextModule(final TestClass testClass, final Object target) {
        super();
        this.target = target;
        this.fields = filterFields(ReflectionUtils.getDeclaredFieldsRecursive(testClass.getJavaClass(), null));
    }

    public void injectFields(final Injector injector) {
        for (Field field : fields) {
            Object value = injector.getInstance(Key.get(TypeLiteral.get(field.getGenericType())));
            ReflectionUtils.setFieldValue(field, target, value);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void configure() {
        for (Field field : fields) {
            AnnotatedBindingBuilder bindingBuilder = bind(TypeLiteral.get(field.getGenericType()));
            ImplementedBy implementedBy = field.getAnnotation(ImplementedBy.class);
            if (implementedBy != null) {
                bindingBuilder.to(implementedBy.value());
            }
            bindingBuilder.in(Singleton.class);
        }
    }

    protected boolean isIgnoredField(final Field field) {
        return (field.getType().isPrimitive() ||
                field.getAnnotation(Mock.class) != null ||
                field.getAnnotation(com.google.inject.Inject.class) != null ||
                field.getAnnotation(javax.inject.Inject.class) != null);
    }

    protected boolean isUninstantiable(final Field field) {
        return (Modifier.isInterface(field.getType().getModifiers()) ||
                Modifier.isAbstract(field.getType().getModifiers()));
    }

    private List<Field> filterFields(final List<Field> fields) {
        List<Field> filtered = new ArrayList<Field>(fields.size());
        for (Field field : fields) {
            if (isIgnoredField(field)) {
                continue;
            }
            if (isUninstantiable(field) && field.getAnnotation(ImplementedBy.class) == null) {
                throw new TestLifeCycleException("Interface or abstract type for field " + field.getName() + " defined without ImplementedBy annotation");
            }
            filtered.add(field);
        }
        return filtered;
    }
}
