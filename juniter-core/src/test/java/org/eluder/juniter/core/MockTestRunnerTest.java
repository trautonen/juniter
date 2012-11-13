package org.eluder.juniter.core;

import java.util.List;

import junit.framework.Assert;

import org.eluder.juniter.core.Mock;
import org.eluder.juniter.core.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(MockTestRunner.class)
public class MockTestRunnerTest {

    @Mock
    private List<String> listMock;

    @Before
    public void before() {
        Assert.assertNotNull(listMock);
    }

    @After
    public void after() {
        Assert.assertNotNull(listMock);
        listMock = null;
    }

    @Test
    public void testFirst() {
        Assert.assertNotNull(listMock);
        Mockito.when(listMock.get(4)).thenReturn("bar");
        Assert.assertEquals("bar", listMock.get(4));
    }

    @Test
    public void testSecond() {
        Assert.assertNotNull(listMock);
        Mockito.when(listMock.get(8)).thenReturn("barbababa");
        Assert.assertEquals("barbababa", listMock.get(8));
    }

}
