package org.eluder.juniter.core.mock;

public interface Mocker {

    <T> T mock(Class<T> type);
}
