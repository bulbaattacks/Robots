package org.example.robot;

import org.example.Task;

public interface Robot {
    void doWork(Task task);
    void shutDown();
    Robot.Type getType();
    boolean isBusy();
    enum Type {CLEANER, TERMINATOR}
}
