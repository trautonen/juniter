package org.eluder.juniter.guice;

import java.io.InputStream;
import java.nio.channels.Channel;

import org.eluder.juniter.core.Mock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@RunWith(GuiceTestRunner.class)
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
