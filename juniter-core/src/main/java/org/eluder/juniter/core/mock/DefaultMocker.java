package org.eluder.juniter.core.mock;

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

import org.eluder.juniter.core.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMocker implements Mocker {

    private static final Logger log = LoggerFactory.getLogger(DefaultMocker.class);

    private Mocker delegate;

    public DefaultMocker() {
        trySetDelegate("org.mockito.Mockito", MockitoMocker.class);
        trySetDelegate("org.easymock.EasyMock", EasyMockMocker.class);
        if (delegate == null) {
            throw new IllegalStateException("No supported mock frameworks found from classpath");
        }
    }

    @Override
    public <T> T mock(final Class<T> type) {
        return delegate.mock(type);
    }

    private void trySetDelegate(final String mockFrameworkType, final Class<? extends Mocker> mockerType) {
        if (delegate == null) {
            try {
                Class.forName(mockFrameworkType);
                delegate = ReflectionUtils.instantiate(mockerType);
                log.debug("{} set as mock framework with {}", mockFrameworkType, mockerType.getName());
            } catch (ClassNotFoundException ex) {
                log.debug("{} not found from classpath, not using it", mockFrameworkType);
            }
        }
    }
}
