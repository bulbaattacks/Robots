package org.example;

import org.example.robot.Robot;

public class Task {
    public final Type taskType;
    public final Action actionType;
    public final Robot.Type robotType;
    public String payload;

    private Task(Action actionType, Task.Type taskType, Robot.Type robotType) {
        this.actionType = actionType;
        this.taskType = taskType;
        this.robotType = robotType;
    }

    private Task(Action actionType, Task.Type taskType, Robot.Type robotType, String payload) {
        this(actionType, taskType, robotType);
        this.payload = payload;
    }

    public static Task work(Robot.Type robotType, String payload) {
        return new Task(Action.DO_WORK, Type.SINGLE, robotType, payload);
    }

    public static Task shutDown(Robot.Type robotType) {
        return new Task(Action.SHUT_DOWN, Type.SINGLE, robotType);
    }

    public static Task workForAll(String payload) {
        return new Task(Action.DO_WORK, Type.BROADCAST, null, payload);
    }

    public static Task shutDownForAll() {
        return new Task(Action.SHUT_DOWN, Type.BROADCAST, null);
    }

    public enum Type {SINGLE, BROADCAST}
    public enum Action {DO_WORK, SHUT_DOWN}
}
