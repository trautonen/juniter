package org.eluder.testutils.core.invoker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import org.eluder.testutils.core.TestLifeCycle;
import org.junit.runners.model.FrameworkMethod;

public class MagicFrameWorkMethod extends FrameworkMethod {

    private final FrameworkMethod delegate;
    private final List<TestLifeCycle> testLifeCycles;

    public MagicFrameWorkMethod(final FrameworkMethod delegate, final List<TestLifeCycle> testLifeCycles) {
        super(delegate.getMethod());
        this.delegate = delegate;
        this.testLifeCycles = testLifeCycles;
    }

    public List<TestLifeCycle> getTestLifeCycles() {
        return testLifeCycles;
    }

    @Override
    public Method getMethod() {
        return delegate.getMethod();
    }

    @Override
    public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
        return delegate.invokeExplosively(target, params);
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public boolean isShadowedBy(final FrameworkMethod other) {
        return delegate.isShadowedBy(other);
    }

    @Override
    public boolean equals(final Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean producesType(final Type type) {
        return delegate.producesType(type);
    }

    @Override
    public Annotation[] getAnnotations() {
        return delegate.getAnnotations();
    }

    @Override
    public <T extends Annotation> T getAnnotation(final Class<T> annotationType) {
        return delegate.getAnnotation(annotationType);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public void validatePublicVoidNoArg(final boolean isStatic, final List<Throwable> errors) {
        delegate.validatePublicVoidNoArg(isStatic, errors);
    }

    @Override
    public void validatePublicVoid(final boolean isStatic, final List<Throwable> errors) {
        delegate.validatePublicVoid(isStatic, errors);
    }

    @Override
    public void validateNoTypeParametersOnArgs(final List<Throwable> errors) {
        delegate.validateNoTypeParametersOnArgs(errors);
    }
}
