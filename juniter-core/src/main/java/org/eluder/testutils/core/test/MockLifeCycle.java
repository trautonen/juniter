package org.eluder.testutils.core.test;

import java.util.List;

import org.eluder.testutils.core.Mock;
import org.eluder.testutils.core.mock.DefaultMocker;
import org.eluder.testutils.core.mock.Mocker;
import org.eluder.testutils.core.util.ReflectionUtils;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockLifeCycle extends BaseLifeCycle {

    private static final Logger log = LoggerFactory.getLogger(MockLifeCycle.class);

    private final Mocker mocker;

    public MockLifeCycle() {
        this.mocker = createMocker();
        if (this.mocker == null) {
            throw new IllegalStateException("Mocker not created properly");
        } else {
            log.debug("{} set as mocker", this.mocker.getClass().getName());
        }
    }

    protected Mocker createMocker() {
        return new DefaultMocker();
    }

    @Override
    public void onBefore(final TestClass testClass, final FrameworkMethod method, final Object target) {
        List<FrameworkField> mockFields = testClass.getAnnotatedFields(Mock.class);
        for (FrameworkField mockField : mockFields) {
            Object mock = mocker.mock(mockField.getType());
            ReflectionUtils.setFieldValue(mockField.getField(), target, mock);
        }
    }
}
