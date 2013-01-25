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

import java.io.InputStream;
import java.nio.channels.Channel;

import org.eluder.juniter.core.Mock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@RunWith(GuiceTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GuiceTestRunnerTest {

    @Mock
    @Named("test.stream")
    private InputStream inputStreamMock;

    @Mock
    private Channel channelMock;

    @Mock
    private Exception exceptionMock;

    @Inject
    private BeanUnderTest beanUnderTest;

    @Before
    public void before() {
        Mockito.when(inputStreamMock.markSupported()).thenReturn(true);
    }

    @Test
    public void testFirst() {
        Mockito.when(exceptionMock.getMessage()).thenReturn("");
        Mockito.when(channelMock.isOpen()).thenReturn(false);
        Assert.assertTrue(beanUnderTest.isValue());
    }

    @Test
    public void testSecond() {
        Mockito.when(exceptionMock.getMessage()).thenReturn(null);
        Mockito.when(channelMock.isOpen()).thenReturn(false);
        Assert.assertFalse(beanUnderTest.isValue());
    }

    public static class BeanUnderTest {
        @Inject
        @Named("test.stream")
        private InputStream inputStream;

        @Inject
        private Channel channel;

        @Inject
        private Exception exception;

        public boolean isValue() {
            if (exception.getMessage() == null) {
                return false;
            }
            if (channel.isOpen()) {
                return false;
            }
            return inputStream.markSupported();
        }
    }

    public static class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(BeanUnderTest.class).in(Singleton.class);
        }
    }

}
