package org.eluder.juniter.core.mock;

import org.easymock.EasyMock;

public class EasyMockMocker implements Mocker {

    @Override
    public <T> T mock(final Class<T> type) {
        return EasyMock.createNiceMock(type);
    }
}
