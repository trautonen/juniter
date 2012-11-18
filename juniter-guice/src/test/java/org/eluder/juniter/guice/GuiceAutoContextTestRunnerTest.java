package org.eluder.juniter.guice;

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
