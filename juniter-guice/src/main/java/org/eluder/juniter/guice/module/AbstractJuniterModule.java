package org.eluder.juniter.guice.module;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.eluder.juniter.core.TestLifeCycleException;
import org.eluder.juniter.core.util.ReflectionUtils;
import org.eluder.juniter.guice.ImplementedBy;
import org.eluder.juniter.guice.ProvidedBy;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.name.Names;

public abstract class AbstractJuniterModule extends AbstractModule {

    private final Object target;
    
    public AbstractJuniterModule(final Object target) {
        this.target = target;
    }
    
    protected final Object getTarget() {
        return target;
    }

    protected void inject(final Field field, final Injector injector) {
        if (!Modifier.isFinal(field.getModifiers())) {
            Key<?> key = null;
            Annotation bindingAnnotation = getBindingAnnotation(field);
            String name = getName(field);
            if (bindingAnnotation != null) {
                key = Key.get(TypeLiteral.get(field.getGenericType()), bindingAnnotation);
            } else if (name != null) {
                key = Key.get(TypeLiteral.get(field.getGenericType()), Names.named(name));
            } else {
                key = Key.get(TypeLiteral.get(field.getGenericType()));
            }
            Object value = injector.getInstance(key);
            ReflectionUtils.setFieldValue(field, getTarget(), value);
        }
    }
    
    @SuppressWarnings("rawtypes")
    protected void bind(final Field field) {
        validateField(field);
        AnnotatedBindingBuilder bindingBuilder = bind(TypeLiteral.get(field.getGenericType()));
        bindAnnotated(field, bindingBuilder);
        bindImplementation(field, bindingBuilder);
    }
    
    @SuppressWarnings("rawtypes")
    protected AnnotatedBindingBuilder bindAnnotated(final Field field, final AnnotatedBindingBuilder bindingBuilder) {
        Annotation bindingAnnotation = getBindingAnnotation(field);
        if (bindingAnnotation != null) {
            bindingBuilder.annotatedWith(bindingAnnotation);
        }
        String name = getName(field);
        if (name != null) {
            bindingBuilder.annotatedWith(Names.named(name));
        }
        return bindingBuilder;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected AnnotatedBindingBuilder bindImplementation(final Field field, final AnnotatedBindingBuilder bindingBuilder) {
        ImplementedBy implementedBy = field.getAnnotation(ImplementedBy.class);
        if (implementedBy != null) {
            bindingBuilder.to(implementedBy.value());
        }
        ProvidedBy providedBy = field.getAnnotation(ProvidedBy.class);
        if (providedBy != null) {
            bindingBuilder.toProvider(providedBy.value());
        }
        Object value = ReflectionUtils.getFieldValue(field, getTarget());
        if (value != null && Modifier.isFinal(field.getModifiers())) {
            bindingBuilder.toInstance(value);
        } else {
            bindingBuilder.in(Singleton.class);
        }
        return bindingBuilder;
    }
    
    protected Annotation getBindingAnnotation(final Field field) {
        for (Annotation annotation : field.getAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (!com.google.inject.name.Named.class.isAssignableFrom(type) &&
                    type.getAnnotation(BindingAnnotation.class) != null) {
                return annotation;
            }
        }
        return null;
    }
    
    protected String getName(final Field field) {
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
    
    protected void validateField(final Field field) {
        if (field.getAnnotation(ImplementedBy.class) != null && field.getAnnotation(ProvidedBy.class) != null) {
            throw new TestLifeCycleException("Field " + field.getName() + " defines both provider and concrete implementation, but only one can be defined");
        }
        if (field.getAnnotation(com.google.inject.name.Named.class) != null && field.getAnnotation(javax.inject.Named.class) != null) {
            throw new TestLifeCycleException("Field " + field.getName() + " defines both Guice and CDI Named annotations, but only one can be defined");
        }
        if (getName(field) != null && getBindingAnnotation(field) != null) {
            throw new TestLifeCycleException("Field " + field.getName() + " defines both @Named annotation and binding annotation, but only one can be defined");
        }
        Object value = ReflectionUtils.getFieldValue(field, getTarget());
        if (value != null && (field.getAnnotation(ImplementedBy.class) != null || field.getAnnotation(ProvidedBy.class) != null)) {
            throw new TestLifeCycleException("Field " + field.getName() + " has value, but provider or concrete implementation is defined");
        }
    }
}
