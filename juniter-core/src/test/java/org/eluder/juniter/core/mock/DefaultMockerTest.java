package org.eluder.juniter.core.mock;

import java.io.InputStream;

import org.eluder.juniter.core.mock.DefaultMocker;
import org.junit.Assert;
import org.junit.Test;

public class DefaultMockerTest {

    @Test
    public void testConstructor() {
        new DefaultMocker();
    }

    @Test
    public void testMock() {
        InputStream mock = new DefaultMocker().mock(InputStream.class);
        Assert.assertNotNull(mock);
    }
}
