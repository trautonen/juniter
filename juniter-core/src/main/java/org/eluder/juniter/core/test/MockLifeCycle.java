package org.eluder.juniter.core.test;

import java.util.List;

import org.eluder.juniter.core.Mock;
import org.eluder.juniter.core.TestLifeCycleException;
import org.eluder.juniter.core.mock.DefaultMocker;
import org.eluder.juniter.core.mock.Mocker;
import org.eluder.juniter.core.util.ReflectionUtils;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test life cycle that mocks all {@link Mock} annotated fields in test class and in its super
 * classes. Custom mocker can be provided overriding the {@link #createMocker()} method.
 */
public class MockLifeCycle extends BaseLifeCycle {

    private static final Logger log = LoggerFactory.getLogger(MockLifeCycle.class);

    private final Mocker mocker;

    public MockLifeCycle() {
        this.mocker = createMocker();
        if (this.mocker == null) {
            throw new TestLifeCycleException("Mocker not created properly");
        } else {
            log.debug("{} set as mocker", this.mocker.getClass().getName());
        }
    }

    /**
     * Creates the mocker.
     */
    protected Mocker createMocker() {
        return new DefaultMocker();
    }

    @Override
    public void onBeforeBefore(final TestClass testClass, final FrameworkMethod method, final Object target) {
        List<FrameworkField> mockFields = testClass.getAnnotatedFields(Mock.class);
        for (FrameworkField mockField : mockFields) {
            Object mock = mocker.mock(mockField.getType());
            ReflectionUtils.setFieldValue(mockField.getField(), target, mock);
        }
    }
}
