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

import java.lang.reflect.Field;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Assert;
import org.junit.Test;

public class ReflectionUtilsTest {

    @Test
    public void testInstantiate() {
        ReflectionTestClass value = ReflectionUtils.instantiate(ReflectionTestClass.class);
        Assert.assertNotNull(value);
    }

    @Test
    public void testSetFieldValue() throws SecurityException, NoSuchFieldException {
        ReflectionTestClass target = new ReflectionTestClass();
        ReflectionUtils.setFieldValue(ReflectionTestClass.class.getDeclaredField("foo"), target, "foo");
        Assert.assertEquals("foo", target.getFoo());
        ReflectionUtils.setFieldValue(ReflectionTestClass.class.getDeclaredField("bar"), target, "bar");
        Assert.assertEquals("bar", target.bar);
    }

    @Test
    public void testGetFieldValue() throws SecurityException, NoSuchFieldException {
        ReflectionTestClass target = new ReflectionTestClass();
        target.setFoo("foo");
        Assert.assertEquals("foo", ReflectionUtils.getFieldValue(ReflectionTestClass.class.getDeclaredField("foo"), target));
        target.bar = "bar";
        Assert.assertEquals("bar", ReflectionUtils.getFieldValue(ReflectionTestClass.class.getDeclaredField("bar"), target));
    }

    @Test
    public void testGetAnnotationsRecursive() {
        List<XmlRootElement> annotations = ReflectionUtils.getAnnotationsRecursive(ReflectionTestClass.class, XmlRootElement.class);
        Assert.assertEquals(2, annotations.size());
        Assert.assertEquals("inherited", annotations.get(0).name());
        Assert.assertEquals("super", annotations.get(1).name());
    }

    @Test
    public void testGetDeclaredClassesRecursive() {
        List<Class<Object>> declaredClassesAll = ReflectionUtils.getDeclaredClassesRecursive(ReflectionTestClass.class, null);
        Assert.assertEquals(3, declaredClassesAll.size());
        List<Class<ReflectionTestClass>> declaredClassesOfType = ReflectionUtils.getDeclaredClassesRecursive(ReflectionTestClass.class, ReflectionTestClass.class);
        Assert.assertEquals(2, declaredClassesOfType.size());
    }

    @Test
    public void testGetDeclaredFieldsRecursive() {
        List<Field> declaredFieldsAll = ReflectionUtils.getDeclaredFieldsRecursive(ReflectionTestClass.class, null);
        Assert.assertEquals(3, declaredFieldsAll.size());
        List<Field> declaredFieldsOfType = ReflectionUtils.getDeclaredFieldsRecursive(ReflectionTestClass.class, String.class);
        Assert.assertEquals(2, declaredFieldsOfType.size());
    }

    @XmlRootElement(name = "inherited")
    public static class ReflectionTestClass extends ReflectionSuperClass {

        private String foo;
        public String bar;

        public String getFoo() {
            return foo;
        }

        public void setFoo(final String foo) {
            this.foo = foo;
        }

        @SuppressWarnings("unused")
        private static class Test1 extends ReflectionTestClass { }
    }

    @XmlRootElement(name = "super")
    public static class ReflectionSuperClass {

        @SuppressWarnings("unused")
        private Long longField;

        @SuppressWarnings("unused")
        private static abstract class Test3 extends ReflectionTestClass { }

        protected static class Test2 { }
    }
}
