package org.example;

import org.example.robot.Robot;

public class Task {
    final Task.Type taskType;
    final Robot.Type robotType;
    public String payload;

    private Task(Type taskType, Robot.Type robotType) {
        this.taskType = taskType;
        this.robotType = robotType;
    }

    private Task(Type taskType, Robot.Type robotType, String payload) {
        this(taskType, robotType);
        this.payload = payload;
    }

    public static Task work(Robot.Type robotType, String payload) {
        return new Task(Type.DO_WORK, robotType, payload);
    }

    public static Task shutDown(Robot.Type robotType) {
        return new Task(Type.SHUT_DOWN, robotType);
    }

    public enum Type {DO_WORK, SHUT_DOWN}
}
