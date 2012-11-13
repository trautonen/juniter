package org.eluder.juniter.core.runner;

import org.eluder.juniter.core.TestLifeCycle;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public interface Runner extends Filterable {

    void init(Class<?> testClass, Class<? extends TestLifeCycle>... lifeCycles) throws InitializationError;

    Description getDescription();

    void run(RunNotifier notifier);

}
