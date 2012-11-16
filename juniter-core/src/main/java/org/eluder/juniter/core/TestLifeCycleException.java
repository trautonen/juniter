package org.eluder.juniter.core;

public class TestLifeCycleException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TestLifeCycleException() {
        super();
    }

    public TestLifeCycleException(final String message) {
        super(message);
    }

    public TestLifeCycleException(final Throwable cause) {
        super(cause);
    }

    public TestLifeCycleException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
