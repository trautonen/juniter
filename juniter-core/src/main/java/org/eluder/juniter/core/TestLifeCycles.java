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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eluder.juniter.core.runner.BasicRunner;
import org.eluder.juniter.core.runner.TestLifeCycleBlockRunner;

/**
 * Annotation that defines test life cycles to be executed on test run.
 * This annotation can be set on the test method or the test class or any
 * super class of the test class.
 * <p>
 * The default order of life cycle initialization with {@link TestLifeCycles}
 * is as follows:
 * <ol>
 * <li>test method
 * <li>test class
 * <li>super class of test class
 * <li>super class of super class of test class
 * <li>...
 * </ol>
 * The default order of life cycle initialization can be changed by overriding
 * {@link TestLifeCycleBlockRunner#getTestLifeCycles} or providing custom
 * block runner at {@link BasicRunner#createDelegate}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface TestLifeCycles {

    /**
     * Array of test life cycles that will be executed in the array order.
     */
    Class<? extends TestLifeCycle>[] value() default {};

}
