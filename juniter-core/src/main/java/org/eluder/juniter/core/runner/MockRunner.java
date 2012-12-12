package org.eluder.juniter.core.runner;

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

import java.util.ArrayList;
import java.util.List;

import org.eluder.juniter.core.TestLifeCycle;
import org.eluder.juniter.core.test.MockLifeCycle;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class MockRunner extends BasicRunner {

    @Override
    protected TestLifeCycleBlockRunner createDelegate(final Class<?> testClass) throws InitializationError {
        final Class<? extends TestLifeCycle> mockLifeCycle = getMockLifeCycle();
        return new TestLifeCycleBlockRunner(testClass) {
            @Override
            protected List<Class<? extends TestLifeCycle>> getTestLifeCycles(final FrameworkMethod method) {
                List<Class<? extends TestLifeCycle>> testLifeCycles = new ArrayList<Class<? extends TestLifeCycle>>();
                testLifeCycles.add(mockLifeCycle);
                testLifeCycles.addAll(super.getTestLifeCycles(method));
                return testLifeCycles;
            }
        };
    }

    protected Class<? extends TestLifeCycle> getMockLifeCycle() {
        return MockLifeCycle.class;
    }
}
