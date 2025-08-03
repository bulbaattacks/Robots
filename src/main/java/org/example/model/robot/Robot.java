package org.example.model.robot;

import org.example.model.Task;

public interface Robot {
    void doWork(Task task);
    void shutDown();
    Robot.Type getType();
    String getCompositeKey();
    boolean isBusy();
    enum Type {CLEANER, TERMINATOR}
}
