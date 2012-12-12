package org.eluder.juniter.core.util;

/*
 * #[license]
 * juniter-core
 * %%
 * Copyright (C) 2012 Tapio Rautonen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * %[license]
 */

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class ReflectionUtils {

    protected ReflectionUtils() {
        // hide constructor
    }

    public static <T> T instantiate(final Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException ex) {
            throw new IllegalStateException(ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static void setFieldValue(final Field field, final Object target, final Object value) {
        doAsAccessible(field, new AsAccessible() {
            @Override
            public Object apply(final Field field) throws IllegalAccessException {
                field.set(target, value);
                return null;
            }
        });
    }

    public static Object getFieldValue(final Field field, final Object target) {
        return doAsAccessible(field, new AsAccessible() {
            @Override
            public Object apply(final Field field) throws IllegalAccessException {
                return field.get(target);
            }
        });
    }

    public static <T extends Annotation> List<T> getAnnotationsRecursive(final Class<?> type, final Class<T> annotationType) {
        List<T> annotations = new ArrayList<T>();
        Class<?> current = type;
        while (!Object.class.equals(current)) {
            T annotation = current.getAnnotation(annotationType);
            if (annotation != null) {
                annotations.add(annotation);
            }
            current = current.getSuperclass();
        }
        return annotations;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<Class<T>> getDeclaredClassesRecursive(final Class<?> type, final Class<T> ofType) {
        List<Class<T>> declaredClasses = new ArrayList<Class<T>>();
        Class<?> current = type;
        while (!Object.class.equals(current)) {
            for (Class<?> declaredClass : current.getDeclaredClasses()) {
                if (ofType == null || ofType.isAssignableFrom(declaredClass)) {
                    declaredClasses.add((Class<T>) declaredClass);
                }
            }
            current = current.getSuperclass();
        }
        return declaredClasses;
    }

    public static List<Field> getDeclaredFieldsRecursive(final Class<?> type, final Class<?> ofType) {
        List<Field> declaredFields = new ArrayList<Field>();
        Class<?> current = type;
        while (!Object.class.equals(current)) {
            for (Field field : current.getDeclaredFields()) {
                if (ofType == null || ofType.isAssignableFrom(field.getType())) {
                    declaredFields.add(field);
                }
            }
            current = current.getSuperclass();
        }
        return declaredFields;
    }

    private static Object doAsAccessible(final Field field, final AsAccessible asAccessible) {
        boolean accessible = field.isAccessible();
        try {
            if (!accessible) {
                field.setAccessible(true);
            }
            return asAccessible.apply(field);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        } finally {
            if (accessible != field.isAccessible()) {
                field.setAccessible(!accessible);
            }
        }
    }

    private static interface AsAccessible {
        Object apply(Field field) throws IllegalAccessException;
    }
}
