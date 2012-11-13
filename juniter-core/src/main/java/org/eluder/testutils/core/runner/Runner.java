package org.eluder.testutils.core.runner;

import org.eluder.testutils.core.TestLifeCycle;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public interface Runner extends Filterable {

    void init(Class<?> testClass, Class<? extends TestLifeCycle>... lifeCycles) throws InitializationError;

    Description getDescription();

    void run(RunNotifier notifier);

}
