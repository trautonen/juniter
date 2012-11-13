package org.eluder.testutils.core.test;

import java.io.InputStream;
import java.util.List;

import org.eluder.testutils.core.Mock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.model.TestClass;
import org.mockito.Mockito;

public class MockLifeCycleTest {

    @Mock
    private InputStream inputStreamMock;

    @Mock
    public List<String> publicList;

    private Iterable<Integer> iterable;

    @Test
    public void testBefore() {
        new MockLifeCycle().onBefore(new TestClass(getClass()), null, this);
        Assert.assertNotNull(inputStreamMock);
        Assert.assertNotNull(publicList);
        Assert.assertNull(iterable);

        Mockito.when(publicList.get(66)).thenReturn("foo");
        Assert.assertEquals("foo", publicList.get(66));
    }
}
