package org.eluder.juniter.core;

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

import org.eluder.juniter.core.TestRunnerTest.FirstLifeCycle;
import org.eluder.juniter.core.TestRunnerTest.SecondLifeCycle;
import org.eluder.juniter.core.test.BaseLifeCycle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

@RunWith(TestRunner.class)
@TestLifeCycles({ FirstLifeCycle.class, SecondLifeCycle.class })
public class TestRunnerTest {

    private boolean startFirst = false;
    private boolean beforeFirst = false;
    private boolean afterFirst = false;
    private boolean endFirst = false;
    private boolean startSecond = false;
    private boolean beforeSecond = false;
    private boolean afterSecond = false;
    private boolean endSecond = false;

    public boolean isStartFirst() {
        return startFirst;
    }

    public void setStartFirst(final boolean startFirst) {
        this.startFirst = startFirst;
    }

    public boolean isBeforeFirst() {
        return beforeFirst;
    }

    public void setBeforeFirst(final boolean beforeFirst) {
        this.beforeFirst = beforeFirst;
    }

    public boolean isAfterFirst() {
        return afterFirst;
    }

    public void setAfterFirst(final boolean afterFirst) {
        this.afterFirst = afterFirst;
    }

    public boolean isEndFirst() {
        return endFirst;
    }

    public void setEndFirst(final boolean endFirst) {
        this.endFirst = endFirst;
    }

    public boolean isStartSecond() {
        return startSecond;
    }

    public void setStartSecond(final boolean startSecond) {
        this.startSecond = startSecond;
    }

    public boolean isBeforeSecond() {
        return beforeSecond;
    }

    public void setBeforeSecond(final boolean beforeSecond) {
        this.beforeSecond = beforeSecond;
    }

    public boolean isAfterSecond() {
        return afterSecond;
    }

    public void setAfterSecond(final boolean afterSecond) {
        this.afterSecond = afterSecond;
    }

    public boolean isEndSecond() {
        return endSecond;
    }

    public void setEndSecond(final boolean endSecond) {
        this.endSecond = endSecond;
    }

    @Before
    public void before() {
        Assert.assertFalse(isBeforeFirst());
        Assert.assertFalse(isBeforeSecond());
    }

    @After
    public void after() {
        Assert.assertTrue(isAfterSecond());
        Assert.assertTrue(isAfterFirst());
    }

    @Test
    public void test() {
        Assert.assertTrue(true);
    }

    public static abstract class AbstractLifeCycle extends BaseLifeCycle {
        @Override
        public void onBeforeBefore(final TestClass testClass, final FrameworkMethod method, final Object target) {
            assertBasic(testClass, method, target);
            beforeInternal((TestRunnerTest) target);
        }

        protected abstract void beforeInternal(TestRunnerTest test);

        @Override
        public void onBeforeTest(final TestClass testClass, final FrameworkMethod method, final Object target) {
            assertBasic(testClass, method, target);
            beforeTestInternal((TestRunnerTest) target);
        }

        protected abstract void beforeTestInternal(TestRunnerTest test);

        @Override
        public void onAfterTest(final TestClass testClass, final FrameworkMethod method, final Object target) {
            assertBasic(testClass, method, target);
            afterTestInternal((TestRunnerTest) target);
        }

        protected abstract void afterTestInternal(TestRunnerTest test);

        @Override
        public void onAfterAfter(final TestClass testClass, final FrameworkMethod method, final Object target) {
            assertBasic(testClass, method, target);
            afterInternal((TestRunnerTest) target);
        }

        protected abstract void afterInternal(TestRunnerTest test);

        private void assertBasic(final TestClass testClass, final FrameworkMethod method, final Object target) {
            Assert.assertEquals("org.eluder.juniter.core.TestRunnerTest", testClass.getName());
            Assert.assertEquals("test", method.getName());
            Assert.assertEquals(TestRunnerTest.class, target.getClass());
        }
    }

    public static class FirstLifeCycle extends AbstractLifeCycle {

        @Override
        protected void beforeInternal(final TestRunnerTest test) {
            Assert.assertFalse(test.isStartFirst());
            Assert.assertFalse(test.isStartSecond());
            test.setStartFirst(true);
        }

        @Override
        protected void beforeTestInternal(final TestRunnerTest test) {
            Assert.assertTrue(test.isStartFirst());
            Assert.assertTrue(test.isStartSecond());
            Assert.assertFalse(test.isBeforeFirst());
            Assert.assertFalse(test.isBeforeSecond());
            test.setBeforeFirst(true);
        }

        @Override
        protected void afterTestInternal(final TestRunnerTest test) {
            Assert.assertTrue(test.isStartFirst());
            Assert.assertTrue(test.isStartSecond());
            Assert.assertTrue(test.isBeforeFirst());
            Assert.assertTrue(test.isBeforeSecond());
            Assert.assertTrue(test.isAfterSecond());
            Assert.assertFalse(test.isAfterFirst());
            test.setAfterFirst(true);
        }

        @Override
        protected void afterInternal(final TestRunnerTest test) {
            Assert.assertTrue(test.isStartFirst());
            Assert.assertTrue(test.isStartSecond());
            Assert.assertTrue(test.isBeforeFirst());
            Assert.assertTrue(test.isBeforeSecond());
            Assert.assertTrue(test.isAfterSecond());
            Assert.assertTrue(test.isAfterFirst());
            Assert.assertTrue(test.isEndSecond());
            Assert.assertFalse(test.isEndFirst());
            test.setEndFirst(true);
        }
    }

    public static class SecondLifeCycle extends AbstractLifeCycle {

        @Override
        protected void beforeInternal(final TestRunnerTest test) {
            Assert.assertTrue(test.isStartFirst());
            Assert.assertFalse(test.isStartSecond());
            Assert.assertFalse(test.isBeforeFirst());
            test.setStartSecond(true);
        }

        @Override
        protected void beforeTestInternal(final TestRunnerTest test) {
            Assert.assertTrue(test.isStartFirst());
            Assert.assertTrue(test.isStartSecond());
            Assert.assertTrue(test.isBeforeFirst());
            Assert.assertFalse(test.isBeforeSecond());
            test.setBeforeSecond(true);
        }

        @Override
        protected void afterTestInternal(final TestRunnerTest test) {
            Assert.assertTrue(test.isStartFirst());
            Assert.assertTrue(test.isStartSecond());
            Assert.assertTrue(test.isBeforeFirst());
            Assert.assertTrue(test.isBeforeSecond());
            Assert.assertFalse(test.isAfterSecond());
            Assert.assertFalse(test.isAfterFirst());
            test.setAfterSecond(true);
        }

        @Override
        protected void afterInternal(final TestRunnerTest test) {
            Assert.assertTrue(test.isStartFirst());
            Assert.assertTrue(test.isStartSecond());
            Assert.assertTrue(test.isBeforeFirst());
            Assert.assertTrue(test.isBeforeSecond());
            Assert.assertTrue(test.isAfterSecond());
            Assert.assertTrue(test.isAfterFirst());
            Assert.assertFalse(test.isEndSecond());
            Assert.assertFalse(test.isEndFirst());
            test.setEndSecond(true);
        }
    }
}
