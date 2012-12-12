package org.eluder.juniter.core.invoker;

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
import java.util.ListIterator;

import org.eluder.juniter.core.TestLifeCycle;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

public class AfterLifeCycleInvoker extends AbstractLifeCycleInvoker {

    private final List<TestLifeCycle> testLifeCycles;

    public AfterLifeCycleInvoker(final Statement base, final TestClass testClass, final FrameworkMethod method, final Object target, final List<TestLifeCycle> testLifeCycles) {
        super(base, testClass, method, target);
        this.testLifeCycles = testLifeCycles;
    }

    @Override
    public void evaluate() throws Throwable {
        List<Throwable> errors = new ArrayList<Throwable>();
        try {
            getBase().evaluate();
        } catch (Throwable th) {
            errors.add(th);
        } finally {
            ListIterator<TestLifeCycle> iter = testLifeCycles.listIterator(testLifeCycles.size());
            while (iter.hasPrevious()) {
                TestLifeCycle testLifeCycle = iter.previous();
                try {
                    testLifeCycle.onAfterAfter(getTestClass(), getMethod(), getTarget());
                } catch (Throwable th) {
                    errors.add(th);
                }
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }
}
