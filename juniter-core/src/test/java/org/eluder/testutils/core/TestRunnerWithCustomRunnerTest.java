package org.eluder.testutils.core;

import java.io.InputStream;
import java.util.List;

import org.eluder.testutils.core.TestRunnerWithCustomRunnerTest.CustomRunner;
import org.eluder.testutils.core.runner.BasicRunner;
import org.eluder.testutils.core.runner.TestLifeCycleBlockRunner;
import org.eluder.testutils.core.test.MockLifeCycle;
import org.eluder.testutils.core.test.TimingLifeCycle;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;

@RunWith(TestRunner.class)
@TestContext(runner = CustomRunner.class, lifeCycles = { TimingLifeCycle.class, MockLifeCycle.class })
public class TestRunnerWithCustomRunnerTest {

    @Mock
    private InputStream inputStreamMock;

    @Test
    public void test1() {
        Assert.assertNotNull(inputStreamMock);
    }

    @Test
    public void test2() {
        Assert.assertNotNull(inputStreamMock);
    }

    public static class CustomRunner extends BasicRunner {
        @Override
        protected TestLifeCycleBlockRunner createDelegate(final Class<?> testClass, final List<Class<? extends TestLifeCycle>> testLifeCycles) throws InitializationError {
            Assert.assertEquals(TestRunnerWithCustomRunnerTest.class, testClass);
            Assert.assertEquals(2, testLifeCycles.size());
            return super.createDelegate(testClass, testLifeCycles);
        }
    }

}
