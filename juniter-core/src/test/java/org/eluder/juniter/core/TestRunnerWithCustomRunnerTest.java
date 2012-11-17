package org.eluder.juniter.core;

import java.io.InputStream;
import java.util.List;

import org.eluder.juniter.core.TestRunnerWithCustomRunnerTest.CustomRunner;
import org.eluder.juniter.core.runner.BasicRunner;
import org.eluder.juniter.core.runner.TestLifeCycleBlockRunner;
import org.eluder.juniter.core.test.MockLifeCycle;
import org.eluder.juniter.core.test.TimingLifeCycle;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

@RunWith(TestRunner.class)
@TestContext(runner = CustomRunner.class)
@TestLifeCycles({ TimingLifeCycle.class, MockLifeCycle.class })
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
        protected TestLifeCycleBlockRunner createDelegate(final Class<?> testClass) throws InitializationError {
            Assert.assertEquals(TestRunnerWithCustomRunnerTest.class, testClass);
            return new TestLifeCycleBlockRunner(testClass) {
                @Override
                protected List<Class<? extends TestLifeCycle>> getTestLifeCycles(final FrameworkMethod method) {
                    List<Class<? extends TestLifeCycle>> testLifeCycles = super.getTestLifeCycles(method);
                    Assert.assertEquals(2, testLifeCycles.size());
                    return testLifeCycles;
                }
            };
        }
    }
}
