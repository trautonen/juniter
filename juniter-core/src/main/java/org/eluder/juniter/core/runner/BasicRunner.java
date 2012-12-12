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

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BasicRunner implements Runner {

    private static final Logger log = LoggerFactory.getLogger(BasicRunner.class);

    private TestLifeCycleBlockRunner delegate;

    @Override
    public final void init(final Class<?> testClass) throws InitializationError {
        delegate = createDelegate(testClass);
        if (delegate == null) {
            throw new InitializationError("JUnit block runner delegate not created properly");
        } else {
            log.debug("{} set as JUnit block runner delegate", delegate.getClass().getName());
        }
    }

    @Override
    public final Description getDescription() {
        return delegate.getDescription();
    }

    @Override
    public final void run(final RunNotifier notifier) {
        delegate.run(notifier);
    }

    @Override
    public final void filter(final Filter filter) throws NoTestsRemainException {
        delegate.filter(filter);
    }

    @Override
    public void sort(final Sorter sorter) {
        delegate.sort(sorter);
    }

    protected TestLifeCycleBlockRunner createDelegate(final Class<?> testClass) throws InitializationError {
        return new TestLifeCycleBlockRunner(testClass);
    }
}
