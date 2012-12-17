package org.eluder.juniter.core;

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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eluder.juniter.core.util.IdentityRegistry;
import org.eluder.juniter.core.util.ReflectionUtils;
import org.junit.Assert;

/**
 * Assertion utility that extends JUnit {@link Assert}.
 */
public class JuniterAssert extends Assert {

    private static final String ITERABLE_INDICATOR = "[]";

    protected JuniterAssert() {
        // hide constructor
    }

    /**
     * Asserts that the actual instance is reflectively equal to the expected instance. The types
     * of the instances must be same or sub types of each other. All fields, including private and
     * protected are compared. The assertions checks only basic data types for equality all other
     * instances in the object graph are asserted against their declared fields. Transient and
     * static fields are ignored.
     * <p>
     * Fields can be ignored by the excluded fields argument. Fields in the object graph follow
     * naming such as 'fieldInRoot', 'fieldInRoot.fieldInInnerInstance'.
     *
     * @param expected the expected instance
     * @param actual the actual instance
     * @param excludedFields the excluded field names
     */
    public static void assertReflectionEquals(final Object expected, final Object actual, final String... excludedFields) {
        assertReflectionEquals(expected, actual, false, excludedFields);
    }

    /**
     * Asserts that the actual instance is reflectively equal to the expected instance. The types
     * of the instances must be same or sub types of each other. All fields, including private and
     * protected are compared. The assertions checks only basic data types for equality all other
     * instances in the object graph are asserted against their declared fields. Transient fields
     * are included or ignored according to the assert transients argument. Static fields are
     * ignored.
     * <p>
     * Fields can be ignored by the excluded fields argument. Fields in the object graph follow
     * naming such as 'fieldInRoot', 'fieldInRoot.fieldInInnerInstance'.
     *
     * @param expected the expected instance
     * @param actual the actual instance
     * @param assertTransients <code>true</code> for including transient fields, otherwise <code>false</code>
     * @param excludedFields the excluded field names
     */
    public static void assertReflectionEquals(final Object expected, final Object actual, final boolean assertTransients, final String... excludedFields) {
        assertReflectionEquals(expected, actual, assertTransients, asSet(excludedFields));
    }

    /**
     * Asserts that the actual instance is reflectively equal to the expected instance. The types
     * of the instances must be same or sub types of each other. All fields, including private and
     * protected are compared. The assertions checks only basic data types for equality all other
     * instances in the object graph are asserted against their declared fields. Transient and
     * static fields are ignored.
     * <p>
     * Fields can be ignored by the excluded fields argument. Fields in the object graph follow
     * naming such as 'fieldInRoot', 'fieldInRoot.fieldInInnerInstance'.
     *
     * @param expected the expected instance
     * @param actual the actual instance
     * @param excludedFields the excluded field names
     */
    public static void assertReflectionEquals(final Object expected, final Object actual, final Set<String> excludedFields) {
        assertReflectionEquals(expected, actual, false, excludedFields);
    }

    /**
     * Asserts that the actual instance is reflectively equal to the expected instance. The types
     * of the instances must be same or sub types of each other. All fields, including private and
     * protected are compared. The assertions checks only basic data types for equality all other
     * instances in the object graph are asserted against their declared fields. Transient fields
     * are included or ignored according to the assert transients argument. Static fields are
     * ignored.
     * <p>
     * Fields can be ignored by the excluded fields argument. Fields in the object graph follow
     * naming such as 'fieldInRoot', 'fieldInRoot.fieldInInnerInstance'.
     *
     * @param expected the expected instance
     * @param actual the actual instance
     * @param assertTransients <code>true</code> for including transient fields, otherwise <code>false</code>
     * @param excludedFields the excluded field names
     */
    public static void assertReflectionEquals(final Object expected, final Object actual, final boolean assertTransients, final Set<String> excludedFields) {
        assertReflectionEquals(null, new IdentityRegistry(), expected, actual, assertTransients, excludedFields);
    }

    /**
     * Asserts that the expected and the actual instance types are related to each other. The
     * types must be same or sub types of each other.
     *
     * @param expected the expected instance
     * @param actual the actual instance
     */
    public static void assertTypeCompatibility(final Object expected, final Object actual) {
        if (isSameOrBothNull(expected, actual)) {
            return;
        }
        if (expected != null && actual == null) {
            Assert.fail("Expected non null instance, but actually was null");
        }
        if (expected == null && actual != null) {
            Assert.fail("Expected null, but actually was non null instance");
        }
        if (getLeafType(expected, actual) == null) {
            Assert.fail("Expected type compatibility with " + expected.getClass().getName() + ", but was not compatible type " + actual.getClass().getName());
        }
    }

    protected static final void assertReflectionEquals(final String fromField, final IdentityRegistry identityRegistry, final Object expected, final Object actual, final boolean assertTransients, final Set<String> excludedFields) {
        if (isSameOrBothNull(expected, actual)) {
            return;
        }
        if (identityRegistry.isRegisteredIdentity(expected, actual)) {
            return;
        }
        assertTypeCompatibility(expected, actual);
        try {
            identityRegistry.registerIdentity(expected, actual);
            Class<?> testType = getLeafType(expected, actual);
            if (isIterableType(testType)) {
                Iterator<?> iterExpected = convertToIterator(expected);
                Iterator<?> iterActual = convertToIterator(actual);
                while (iterExpected.hasNext() && iterActual.hasNext()) {
                    Object expectedItem = iterExpected.next();
                    Object actualItem = iterActual.next();
                    assertReflectionEquals(fromField + ITERABLE_INDICATOR, identityRegistry, expectedItem, actualItem, assertTransients, excludedFields);
                }
                if (iterExpected.hasNext() || iterActual.hasNext()){
                    Assert.fail("Iterable values from field '" + fromField + "' do not contain same number of items");
                }
            } else if (isMap(testType)) {
                Map<?, ?> expectedMap = (Map<?, ?>) expected;
                Map<?, ?> actualMap = (Map<?, ?>) actual;
                if (expectedMap.size() != actualMap.size()) {
                    Assert.fail("Maps from field '" + fromField + "' do not contain same number of entries");
                }
                for (Entry<?, ?> expectedEntry : expectedMap.entrySet()) {
                    if (!actualMap.containsKey(expectedEntry.getKey())) {
                        Assert.fail("Actual map from field '" + fromField + "' does not contain expected key " + expectedEntry.getKey().toString());
                    }
                    assertReflectionEquals(fromField, identityRegistry, expectedEntry.getValue(), actualMap.get(expectedEntry.getKey()), assertTransients, excludedFields);
                }
            } else if (isAssertableType(testType)) {
                String message = null;
                if (fromField != null) {
                    message = "Values from field '" + fromField + "' are not equal,";
                }
                Assert.assertEquals(message, expected, actual);
            } else {
                for (Field field : ReflectionUtils.getDeclaredFieldsRecursive(testType, null)) {
                    if (isAssertableField(fromField, field, assertTransients, excludedFields)) {
                        assertReflectionEquals(getFullFieldName(fromField, field), identityRegistry, ReflectionUtils.getFieldValue(field, expected), ReflectionUtils.getFieldValue(field, actual), assertTransients, excludedFields);
                    }
                }
            }
        } finally {
            identityRegistry.unregisterIdentity(expected, actual);
        }
    }

    protected static final Class<?> getLeafType(final Object o1, final Object o2) {
        Class<?> o1Class = o1.getClass();
        Class<?> o2Class = o2.getClass();
        Class<?> leafType = null;
        if (o1Class.isInstance(o2)) { // o2 <= o1
            leafType = o1Class;
            if (!o2Class.isInstance(o1)) { // o2 < o1
                leafType = o2Class;
            }
        } else if (o2Class.isInstance(o1)) { // o1 <= o2
            leafType = o2Class;
            if (!o1Class.isInstance(o2)) { // o1 < o2
                leafType = o1Class;
            }
        }
        return leafType;
    }

    protected static final Set<String> asSet(final String... values) {
        if (values == null) {
            return Collections.emptySet();
        }
        Set<String> set = new HashSet<String>(values.length);
        for (String value : values) {
            set.add(value);
        }
        return set;
    }

    protected static final Object[] asObjectArray(final Object instance) {
        int length = Array.getLength(instance);
        Object[] outputArray = new Object[length];
        for(int i = 0; i < length; ++i){
           outputArray[i] = Array.get(instance, i);
        }
        return outputArray;
    }

    protected static final boolean isSameOrBothNull(final Object o1, final Object o2) {
        return (o1 == o2 || (o1 == null && o2 == null));
    }

    protected static final boolean isAssertableField(final String fromField, final Field field, final boolean assertTransients, final Set<String> excludedFields) {
        return ((assertTransients || !Modifier.isTransient(field.getModifiers())) &&
                !Modifier.isStatic(field.getModifiers()) &&
                !excludedFields.contains(getFullFieldName(fromField, field).replace(ITERABLE_INDICATOR, "")));
    }

    protected static final String getFullFieldName(final String fromField, final Field field) {
        return (fromField == null ? field.getName() : fromField + "." + field.getName());
    }

    protected static final boolean isAssertableType(final Class<?> type) {
        return (type.isPrimitive() ||
                type.isEnum() ||
                type.getName().startsWith("java.lang") ||
                Date.class.equals(type) ||
                Calendar.class.equals(type));
    }

    protected static final boolean isIterableType(final Class<?> type) {
        return (type.isArray() ||
                Iterable.class.isAssignableFrom(type) ||
                Iterator.class.isAssignableFrom(type));
    }

    protected static final boolean isMap(final Class<?> type) {
        return (Map.class.isAssignableFrom(type));
    }

    protected static final Iterator<?> convertToIterator(final Object instance) {
        if (instance instanceof Iterator) {
            return (Iterator<?>) instance;
        }
        if (instance instanceof Iterable) {
            return ((Iterable<?>) instance).iterator();
        }
        return Arrays.asList(asObjectArray(instance)).iterator();
    }
}
