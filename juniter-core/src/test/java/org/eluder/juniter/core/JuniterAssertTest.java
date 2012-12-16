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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class JuniterAssertTest {

    @Test
    public void testAssertReflectionEqualsWithSameInstances() {
        TestClass1 instance1 = new TestClass1();
        JuniterAssert.assertReflectionEquals(instance1, instance1);
    }

    @Test
    public void testAssertReflectionEqualsWithEqualStrings() {
        JuniterAssert.assertReflectionEquals("test", "test");
    }

    @Test
    public void testAssertReflectionEqualsWithUnequalStrings() {
        assertWithErrorAndContains("test1", "test2", "test[1]", "test[2]");
    }

    @Test
    public void testAssertReflectionEqualsWithCircularReference() {
        TestClass1 instance1 = new TestClass1();
        TestClass1 instance2 = new TestClass1();
        TestClass2 circular1 = new TestClass2();
        TestClass2 circular2 = new TestClass2();
        instance1.another = circular1;
        circular1.circular = instance1;
        instance2.another = circular2;
        circular2.circular = instance2;

        JuniterAssert.assertReflectionEquals(instance1, instance2, (String[]) null);
    }

    @Test
    public void testAssertReflectionEqualsWithUnequalSubField() {
        TestClass1 instance1 = new TestClass1();
        TestClass1 instance2 = new TestClass1();
        TestClass3 another1 = new TestClass3();
        TestClass3 another2 = new TestClass3();
        instance1.yetAnother = another1;
        instance2.yetAnother = another2;
        another1.number = 2.2;
        another2.number = 3.6;

        assertWithErrorAndContains(instance1, instance2, "yetAnother.number");
    }

    @Test
    public void testAssertReflectionEqualsWithIgnoredField() {
        TestClass1 instance1 = new TestClass1();
        TestClass1 instance2 = new TestClass1();
        TestClass3 another1 = new TestClass3();
        TestClass3 another2 = new TestClass3();
        instance1.yetAnother = another1;
        instance2.yetAnother = another2;
        another1.number = 2.2;
        another2.number = 3.6;

        JuniterAssert.assertReflectionEquals(instance1, instance2, "yetAnother.number");
    }

    @Test
    public void testAssertReflectionEqualsWithDifferentSizeLists() {
        TestClass2 instance1 = new TestClass2();
        TestClass2 instance2 = new TestClass2();
        instance1.ints = Arrays.asList(1, 2);
        instance2.ints = Arrays.asList(1);

        assertWithErrorAndContains(instance1, instance2, "Iterable values");
    }

    @Test
    public void testAssertReflectionEqualsWithDifferentValuesInLists() {
        TestClass2 instance1 = new TestClass2();
        TestClass2 instance2 = new TestClass2();
        instance1.ints = Arrays.asList(1, 2);
        instance2.ints = Arrays.asList(1, 3);

        assertWithErrorAndContains(instance1, instance2, "ints[]", "<2>", "<3>");
    }

    @Test
    public void testAssertReflectionEqualsWithDifferentValuesInArray() {
        TestClass2 instance1 = new TestClass2();
        TestClass2 instance2 = new TestClass2();
        instance1.intsArray = new int[] { 2, 4, 5 };
        instance2.intsArray = new int[] { 2, 3, 5 };

        assertWithErrorAndContains(instance1, instance2, "intsArray[]", "<4>", "<3>");
    }

    @Test
    public void testAssertReflectionEqualsWithIterator() {
        TestClass3 instance1 = new TestClass3();
        TestClass3 instance2 = new TestClass3();
        instance1.iterator = Arrays.asList("foo", "bar").iterator();
        instance2.iterator = Arrays.asList("foo", "barbaba").iterator();

        assertWithErrorAndContains(instance1, instance2, "iterator[]", "bar[]", "bar[baba]");
    }

    @Test
    public void testAssertReflectionEqualsWithInnerArrayIgnored() {
        TestClass1 instance1 = new TestClass1();
        TestClass1 instance2 = new TestClass1();
        TestClass2 another1 = new TestClass2();
        TestClass2 another2 = new TestClass2();
        instance1.another = another1;
        instance2.another = another2;
        another1.intsArray = new int[] { 2, 4, 5 };
        another2.intsArray = new int[] { 2, 3, 5 };

        JuniterAssert.assertReflectionEquals(instance1, instance2, "another.intsArray");
    }

    @Test
    public void testAssertReflectionEqualsWithTransientIgnored() {
        TestClass1 instance1 = new TestClass1();
        TestClass1 instance2 = new TestClass1();
        instance1.transientValue = 10;
        instance2.transientValue = 20;

        JuniterAssert.assertReflectionEquals(instance1, instance2);
    }

    @Test(expected = AssertionError.class)
    public void testAssertReflectionEqualsWithTransientAsserted() {
        TestClass1 instance1 = new TestClass1();
        TestClass1 instance2 = new TestClass1();
        instance1.transientValue = 10;
        instance2.transientValue = 20;

        JuniterAssert.assertReflectionEquals(instance1, instance2, true);
    }

    @Test
    public void testAssertTypeCompatibilityWithSameTypes() {
        JuniterAssert.assertTypeCompatibility("foo", "bar");
    }

    @Test
    public void testAssertTypeCompatibilityWithSubTypes() {
        JuniterAssert.assertTypeCompatibility(new TestClass1(), new TestClass3());
    }

    @Test(expected = AssertionError.class)
    public void testAssertTypeCompatibilityWithIncompatibleTypes() {
        JuniterAssert.assertTypeCompatibility(new TestClass1(), new TestClass2());
    }

    @Test(expected = AssertionError.class)
    public void testAssertTypeCompatibilityWithExpectedNull() {
        JuniterAssert.assertTypeCompatibility(null, new TestClass1());
    }

    @Test(expected = AssertionError.class)
    public void testAssertTypeCompatibilityWithActualNull() {
        JuniterAssert.assertTypeCompatibility(new TestClass1(), null);
    }

    @Test
    public void testAssertTypeCompatibilityWithNulls() {
        JuniterAssert.assertTypeCompatibility(null, null);
    }

    private void assertWithErrorAndContains(final Object expected, final Object actual, final String... text) {
        try {
            JuniterAssert.assertReflectionEquals(expected, actual);
            JuniterAssert.fail("Values should not be equal");
        } catch (AssertionError ex) {
            for (String value : text) {
                JuniterAssert.assertTrue(ex.getMessage(), ex.getMessage().contains(value));
            }
        }
    }

    @SuppressWarnings("unused")
    private static class TestClass1 {
        public TestClass2 another;
        public TestClass3 yetAnother;
        public String value;
        public transient int transientValue;
    }

    @SuppressWarnings("unused")
    private static class TestClass2 {
        public TestClass1 circular;
        public List<Integer> ints;
        public int[] intsArray;
    }

    @SuppressWarnings("unused")
    private static class TestClass3 extends TestClass1 {
        private static final String FOO = "bar";
        public Double number;
        public Iterator<String> iterator;
    }
}
