package org.eluder.juniter.guice;

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

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

import org.eluder.juniter.core.Mock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.inject.Inject;

@RunWith(GuiceTestRunner.class)
public class GuiceAutoContextTestRunnerTest {

    @Mock
    private Channel channelMock;

    @Mock
    private List<Integer> integerListMock;

    private BeanUnderTest beanUnderTest;

    @ImplementedBy(ArrayList.class)
    private List<String> genericList;

    @Before
    public void before() {
        Mockito.when(channelMock.isOpen()).thenReturn(true);
        Mockito.when(integerListMock.size()).thenReturn(42);
        genericList.add("foo");
    }

    @Test
    public void test() {
        Assert.assertTrue(beanUnderTest.isOpen());
    }

    @Test
    public void testAddToList() {
        genericList.add("bar");
        Assert.assertEquals(2, genericList.size());
        Assert.assertEquals(2, beanUnderTest.size());
    }

    @Test
    public void testGenericTypeReseted() {
        Assert.assertEquals(1, genericList.size());
        Assert.assertEquals(1, beanUnderTest.size());
    }

    @Test
    public void testConstructorInjection() {
        Assert.assertEquals(42, beanUnderTest.integerListSize());
    }

    public static class BeanUnderTest {
        @Inject
        private Channel channel;

        @Inject
        private List<String> list;

        private final List<Integer> integerList;

        @Inject
        public BeanUnderTest(final List<Integer> integerList) {
            this.integerList = integerList;
        }

        public boolean isOpen() {
            return channel.isOpen();
        }

        public int size() {
            return list.size();
        }

        public int integerListSize() {
            return integerList.size();
        }
    }
}
