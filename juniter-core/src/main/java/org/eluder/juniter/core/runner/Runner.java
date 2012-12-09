package org.eluder.juniter.core.runner;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

/**
 * Interface that abstracts the JUnit {@link org.junit.runner.Runner} behind an interface.
 */
public interface Runner extends Filterable, Sortable {

    /**
     * Initializes the runner.
     *
     * @param testClass the test class
     * @throws InitializationError if initialization fails
     */
    void init(Class<?> testClass) throws InitializationError;

    /**
     * {@link org.junit.runner.Runner#getDescription()}
     */
    Description getDescription();

    /**
     * {@link org.junit.runner.Runner#run(RunNotifier)}
     */
    void run(RunNotifier notifier);

}
