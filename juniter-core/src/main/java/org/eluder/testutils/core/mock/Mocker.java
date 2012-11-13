package org.eluder.testutils.core.mock;

public interface Mocker {

    <T> T mock(Class<T> type);
}
