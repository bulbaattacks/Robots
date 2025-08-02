package org.example;

import org.example.robot.Robot;
import org.example.robot.RobotFactory;

import java.util.*;

public class Manager {
    final Map<Robot.Type, Queue<Robot>> availableRobotMap = new EnumMap<>(Robot.Type.class);
    private final Queue<Task> taskQueue = new ArrayDeque<>();

    public Manager() {
        for (var rt : Robot.Type.values()) {
            if (rt == Robot.Type.UNIVERSAL) continue;
            availableRobotMap.put(rt, new ArrayDeque<>());
        }
    }

    public boolean hasTask() {
        return !taskQueue.isEmpty();
    }

    public void addTaskToQueue(Task task) {
        taskQueue.add(task);
    }

    public void addRobotToMap(Robot robot) {
        var robotQueue = availableRobotMap.computeIfAbsent(robot.getType(), k -> new ArrayDeque<>());
        robotQueue.add(robot);
    }

    public void manageWork() {
        var task = taskQueue.poll();
        var robotType = task.robotType;

        var robot = availableRobotMap.get(robotType).poll();
        if (robot == null) {
            robot = RobotFactory.createRobotWithManager(robotType, this);
        }
        switch (task.taskType) {
            case Task.Type.DO_WORK -> robot.doWork(task);
            case Task.Type.SHUT_DOWN -> robot.shutDown();
        }
    }

    public void notifyTaskCompleted(Robot robot) {
        addRobotToMap(robot);
    }
}
