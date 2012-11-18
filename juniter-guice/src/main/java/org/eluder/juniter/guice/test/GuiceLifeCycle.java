package org.eluder.juniter.guice.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eluder.juniter.core.TestLifeCycleException;
import org.eluder.juniter.core.test.BaseLifeCycle;
import org.eluder.juniter.core.util.ReflectionUtils;
import org.eluder.juniter.guice.GuiceContext;
import org.eluder.juniter.guice.GuiceModules;
import org.eluder.juniter.guice.module.AutoContextModule;
import org.eluder.juniter.guice.module.MockModule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GuiceLifeCycle extends BaseLifeCycle {

    @Override
    public void onBefore(final TestClass testClass, final FrameworkMethod method, final Object target) {
        List<Module> modules = new ArrayList<Module>();
        GuiceContext guiceContext = getGuiceContext(testClass, method);
        if (guiceContext == null || guiceContext.bindMocks()) {
            modules.add(createMockModule(testClass, method, target));
        }
        AutoContextModule autoContextModule = null;
        if (guiceContext == null || guiceContext.autoContext()) {
            autoContextModule = createAutoContextModule(testClass, method, target);
            modules.add(autoContextModule);
        }
        List<Class<? extends Module>> guiceModules = getGuiceModules(testClass, method);
        verifyGuiceModules(guiceModules);
        for (Class<? extends Module> guiceModule : guiceModules) {
            modules.add(ReflectionUtils.instantiate(guiceModule));
        }
        Injector injector = Guice.createInjector(modules);
        injector.injectMembers(target);
        if (autoContextModule != null) {
            autoContextModule.injectFields(injector);
        }
    }

    protected MockModule createMockModule(final TestClass testClass, final FrameworkMethod method, final Object target) {
        return new MockModule(testClass, target);
    }

    protected AutoContextModule createAutoContextModule(final TestClass testClass, final FrameworkMethod method, final Object target) {
        return new AutoContextModule(testClass, target);
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
        for (Class<Module> declaredModule : ReflectionUtils.getDeclaredClassesRecursive(testClass.getJavaClass(), Module.class)) {
            if (!Modifier.isAbstract(declaredModule.getModifiers()) && !Modifier.isInterface(declaredModule.getModifiers())) {
                guiceModules.add(declaredModule);
            }
        }
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
}
