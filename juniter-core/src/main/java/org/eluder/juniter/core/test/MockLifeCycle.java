package org.eluder.juniter.core.test;

/*
 * #[license]
 * juniter-core
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

import java.util.List;

import org.eluder.juniter.core.Mock;
import org.eluder.juniter.core.TestLifeCycleException;
import org.eluder.juniter.core.mock.DefaultMocker;
import org.eluder.juniter.core.mock.Mocker;
import org.eluder.juniter.core.util.ReflectionUtils;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test life cycle that mocks all {@link Mock} annotated fields in test class and in its super
 * classes. Custom mocker can be provided overriding the {@link #createMocker()} method.
 */
public class MockLifeCycle extends BaseLifeCycle {

    private static final Logger log = LoggerFactory.getLogger(MockLifeCycle.class);

    private final Mocker mocker;

    public MockLifeCycle() {
        this.mocker = createMocker();
        if (this.mocker == null) {
            throw new TestLifeCycleException("Mocker not created properly");
        } else {
            log.debug("{} set as mocker", this.mocker.getClass().getName());
        }
    }

    /**
     * Creates the mocker.
     */
    protected Mocker createMocker() {
        return new DefaultMocker();
    }

    @Override
    public void onBeforeBefore(final TestClass testClass, final FrameworkMethod method, final Object target) {
        List<FrameworkField> mockFields = testClass.getAnnotatedFields(Mock.class);
        for (FrameworkField mockField : mockFields) {
            Object mock = mocker.mock(mockField.getType());
            ReflectionUtils.setFieldValue(mockField.getField(), target, mock);
        }
    }
}
