package org.eluder.testutils.core.util;

import java.lang.reflect.Field;


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
