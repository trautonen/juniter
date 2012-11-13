package org.eluder.testutils.core.mock;

import org.mockito.Mockito;


public class MockitoMocker implements Mocker {

    @Override
    public <T> T mock(final Class<T> type) {
        return Mockito.mock(type);
    }

}
