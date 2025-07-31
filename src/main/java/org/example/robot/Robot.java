package org.example.robot;

import org.example.Task;

public interface Robot {
    void doWork(Task task);
    void shutDown();
    Robot.Type getType();
    enum Type {CLEANER, TERMINATOR}
}
