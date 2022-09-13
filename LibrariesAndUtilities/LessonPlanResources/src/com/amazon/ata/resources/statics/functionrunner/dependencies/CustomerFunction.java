package com.amazon.ata.resources.statics.functionrunner.dependencies;

/**
 * Interface for defining a function to be run in the functionrunner framework.
 * A client function implements the interface, providing a {@code run()} method.
 */
public interface CustomerFunction {
    /**
     * Runs the payload defined by the client function in implementing classes.
     */
    void run();
}
