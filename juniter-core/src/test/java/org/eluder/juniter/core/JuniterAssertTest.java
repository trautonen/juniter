package org.eluder.juniter.core;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class JuniterAssertTest {

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

        JuniterAssert.assertReflectionEquals(instance1, instance2);
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
    }

    @SuppressWarnings("unused")
    private static class TestClass2 {
        public TestClass1 circular;
        public List<Integer> ints;
        public int[] intsArray;
    }

    @SuppressWarnings("unused")
    private static class TestClass3 extends TestClass1 {
        public Double number;
    }
}
