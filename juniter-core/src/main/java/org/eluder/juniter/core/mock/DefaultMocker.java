package org.eluder.juniter.core.mock;

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
