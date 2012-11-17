package org.eluder.juniter.guice.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eluder.juniter.core.Mock;
import org.eluder.juniter.core.TestLifeCycleException;
import org.eluder.juniter.core.test.BaseLifeCycle;
import org.eluder.juniter.core.util.ReflectionUtils;
import org.eluder.juniter.guice.GuiceContext;
import org.eluder.juniter.guice.GuiceModules;
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
        List<Module> modules = new ArrayList<Module>();
        GuiceContext guiceContext = getGuiceContext(testClass, method);
        verifyGuiceContext(guiceContext);
        if (guiceContext == null || guiceContext.bindMocks()) {
            modules.add(new MockModule(testClass.getAnnotatedFields(Mock.class), target));
        }
        List<Class<? extends Module>> guiceModules = getGuiceModules(testClass, method);
        verifyGuiceModules(guiceModules);
        for (Class<? extends Module> guiceModule : guiceModules) {
            modules.add(ReflectionUtils.instantiate(guiceModule));
        }
        Injector injector = Guice.createInjector(modules);
        injector.injectMembers(target);
    }

    private List<Class<? extends Module>> getGuiceModules(final TestClass testClass, final FrameworkMethod method) {
        List<Class<? extends Module>> guiceModules = new ArrayList<Class<? extends Module>>();
        GuiceModules methodAnnotation = method.getAnnotation(GuiceModules.class);
        if (methodAnnotation != null) {
            guiceModules.addAll(Arrays.asList(methodAnnotation.value()));
        }
        List<GuiceModules> classAnnotations = ReflectionUtils.getAnnotationsRecursive(testClass.getJavaClass(), GuiceModules.class);
        for (GuiceModules classAnnotation : classAnnotations) {
            guiceModules.addAll(Arrays.asList(classAnnotation.value()));
        }
        guiceModules.addAll(ReflectionUtils.getDeclaredClassesRecursive(testClass.getJavaClass(), Module.class, true));
        return guiceModules;
    }

    private GuiceContext getGuiceContext(final TestClass testClass, final FrameworkMethod method) {
        GuiceContext methodAnnotation = method.getAnnotation(GuiceContext.class);
        if (methodAnnotation != null) {
            return methodAnnotation;
        }
        List<GuiceContext> classAnnotations = ReflectionUtils.getAnnotationsRecursive(testClass.getJavaClass(), GuiceContext.class);
        if (classAnnotations.size() > 0) {
            return classAnnotations.get(0);
        }
        return null;
    }

    private void verifyGuiceContext(final GuiceContext guiceContext) {

    }

    private void verifyGuiceModules(final List<Class<? extends Module>> guiceModules) {
        for (Class<? extends Module> guiceModule : guiceModules) {
            Constructor<?>[] constructors = guiceModule.getConstructors();
            if (constructors.length == 0) {
                throw new TestLifeCycleException(guiceModule.getName() + " does not have a public constructor");
            }
            boolean zeroArg = false;
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterTypes().length == 0) {
                    zeroArg = true;
                    break;
                }
            }
            if (!zeroArg) {
                throw new TestLifeCycleException(guiceModule.getName() + " does not have a zero arg constructor");
            }
        }
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
                    throw new TestLifeCycleException("Mock field " + mockField.getName() + " not initialized, is mock test life cycle run before Guice test life cycle?");
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
