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

import org.eluder.juniter.core.TestLifeCycle;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 * Simple base class for test life cycles. Provides convinient empty methods so that only those
 * that are relevant to the implementation can be implemented.
 */
public class BaseLifeCycle implements TestLifeCycle {

    @Override
    public void onBeforeBefore(final TestClass testClass, final FrameworkMethod method, final Object target) {
        // noop
    }

    @Override
    public void onBeforeTest(final TestClass testClass, final FrameworkMethod method, final Object target) {
        // noop
    }

    @Override
    public void onSuccess(final TestClass testClass, final FrameworkMethod method, final Object target) {
        // noop
    }

    @Override
    public void onFailure(final TestClass testClass, final FrameworkMethod method, final Object target) {
        // noop
    }

    @Override
    public void onAfterTest(final TestClass testClass, final FrameworkMethod method, final Object target) {
        // noop
    }

    @Override
    public void onAfterAfter(final TestClass testClass, final FrameworkMethod method, final Object target) {
        // noop
    }
}
