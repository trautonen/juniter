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
import java.util.List;

import org.eluder.juniter.core.Mock;
import org.eluder.juniter.core.TestLifeCycleException;
import org.eluder.juniter.core.util.ReflectionUtils;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.TestClass;

import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;

public class MockModule extends AbstractJuniterModule {

    private final List<FrameworkField> mockFields;

    public MockModule(final TestClass testClass, final Object target) {
        super(target);
        this.mockFields = testClass.getAnnotatedFields(Mock.class);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void configure() {
        for (FrameworkField mockField : mockFields) {
            Object value = ReflectionUtils.getFieldValue(mockField.getField(), getTarget());
            if (value == null) {
                throw new TestLifeCycleException("Mock field " + mockField.getName() + " not initialized, is mock test life cycle run before Guice test life cycle?");
            } else {
                Field field = mockField.getField();
                validateField(field);
                AnnotatedBindingBuilder bindingBuilder = bind(TypeLiteral.get(field.getGenericType()));
                bindAnnotated(field, bindingBuilder);
                bindingBuilder.toInstance(value);
            }
        }
    }
}
