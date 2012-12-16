package org.eluder.juniter.core.util;

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