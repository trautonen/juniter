package org.eluder.juniter.guice.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eluder.juniter.core.Mock;
import org.eluder.juniter.core.test.BaseLifeCycle;
import org.eluder.juniter.core.util.ReflectionUtils;
import org.eluder.juniter.guice.GuiceContext;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.name.Names;

public class GuiceLifeCycle extends BaseLifeCycle {

    @Override
    public void onBefore(final TestClass testClass, final FrameworkMethod method, final Object target) {
        GuiceContext guiceContext = testClass.getJavaClass().getAnnotation(GuiceContext.class);
        List<Module> modules = new ArrayList<Module>();
        if (guiceContext == null || guiceContext.bindMocks()) {
            modules.add(new MockModule(testClass.getAnnotatedFields(Mock.class), target));
        }
        if (guiceContext != null && guiceContext.modules().length > 0) {
            for (Class<? extends Module> module : guiceContext.modules()) {
                modules.add(ReflectionUtils.instantiate(module));
            }
        }
        Injector injector = Guice.createInjector(modules);
        injector.injectMembers(target);
    }

    private static class MockModule extends AbstractModule {

        private final List<FrameworkField> mockFields;
        private final Object target;

        public MockModule(final List<FrameworkField> mockFields, final Object target) {
            this.mockFields = mockFields;
            this.target = target;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        protected void configure() {
            for (FrameworkField mockField : mockFields) {
                Object value = ReflectionUtils.getFieldValue(mockField.getField(), target);
                if (value == null) {
                    throw new IllegalStateException("Mock field " + mockField.getName() + " not initialized, is mock test life cycle run before Guice test life cycle?");
                } else {
                    String name = getName(mockField.getField());
                    AnnotatedBindingBuilder bindingBuilder = bind(mockField.getType());
                    if (name != null) {
                        bindingBuilder.annotatedWith(Names.named(name));
                    }
                    bindingBuilder.toInstance(value);
                }
            }
        }

        private String getName(final Field field) {
            com.google.inject.name.Named guiceNamed = field.getAnnotation(com.google.inject.name.Named.class);
            if (guiceNamed != null) {
                return guiceNamed.value();
            }
            javax.inject.Named cdiNamed = field.getAnnotation(javax.inject.Named.class);
            if (cdiNamed != null) {
                return cdiNamed.value();
            }
            return null;
        }
    }
}
