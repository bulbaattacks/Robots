package org.example;

import org.example.robot.Robot;

import java.util.*;

public class Manager {
    final Map<Robot.Type, Queue<Robot>> typeRobotMap = new EnumMap<>(Robot.Type.class);
    private final Queue<Task> taskQueue = new ArrayDeque<>();

    public boolean hasTask() {
        return !taskQueue.isEmpty();
    }

    public void addTaskToQueue(Task task) {
        taskQueue.add(task);
    }

    public void addRobotToMap(Robot robot) {
        var robotQueue = typeRobotMap.get(robot.getType());
        if (robotQueue == null) {
            robotQueue = new ArrayDeque<>();
            typeRobotMap.put(robot.getType(), robotQueue);
        }
        robotQueue.add(robot);
    }

    public void doTask() {
        var task = taskQueue.poll();
        var robotType = task.robotType;
        var robot = typeRobotMap.get(robotType).poll();
        if (task.taskType == Task.Type.SHUT_DOWN) {
            robot.shutDown();
        } else {
            robot.doWork(task);
        }
    }
}
