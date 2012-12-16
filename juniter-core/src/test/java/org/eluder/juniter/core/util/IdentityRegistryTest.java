package org.eluder.juniter.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class IdentityRegistryTest {

    private IdentityRegistry registry;

    @Before
    public void init() {
        registry = new IdentityRegistry();
    }

    @Test
    public void testRegisterIdentity() {
        Object o1 = new Object();
        Object o2 = new Object();
        registry.registerIdentity(o1, o2);

        Assert.assertTrue(registry.isRegisteredIdentity(o1, o2));
        Assert.assertTrue(registry.isRegisteredIdentity(o2, o1));
    }

    @Test
    public void testUnregisterIdentity() {
        Object o1 = new Object();
        Object o2 = new Object();
        registry.registerIdentity(o1, o2);

        Assert.assertTrue(registry.isRegisteredIdentity(o1, o2));
        Assert.assertTrue(registry.isRegisteredIdentity(o2, o1));

        registry.unregisterIdentity(o1, o2);

        Assert.assertFalse(registry.isRegisteredIdentity(o1, o2));
        Assert.assertFalse(registry.isRegisteredIdentity(o2, o1));
    }
}
