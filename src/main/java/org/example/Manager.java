package org.example;

import org.example.robot.Robot;
import org.example.robot.RobotFactory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Manager {
    final Map<Robot.Type, BlockingQueue<Robot>> availableRobotMap = new EnumMap<>(Robot.Type.class);
    private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

    public Manager() {
        for (var rt : Robot.Type.values()) {
            availableRobotMap.put(rt, new LinkedBlockingQueue<>());
        }
    }

    public boolean hasTask() {
        return !taskQueue.isEmpty();
    }

    public void addTask(Task task) {
        taskQueue.add(task);
    }

    public void pushBackToMap(Robot robot) {
        availableRobotMap
                .computeIfAbsent(robot.getType(), k -> new LinkedBlockingQueue<>())
                .add(robot);
    }

    public void manageWork() {
        if (!hasTask()) {
            System.out.println("Thread: " + Thread.currentThread().getName() + ", no tasks available");
            return;
        }

        var task = taskQueue.poll();
        switch (task.taskType) {
            case SINGLE -> assignTaskForOneRobot(task);
            case BROADCAST -> assignTaskForAllRobots(task);
        }
    }

    private void assignTaskForOneRobot(Task task) {
        var robotType = task.robotType;
        var robotQueue = availableRobotMap.get(robotType);
        System.out.println("Thread: " + Thread.currentThread().getName() + " task for one robot " + robotType + " --> " + task.actionType);

        var robot = robotQueue.peek();

        if (task.actionType == Task.Action.SHUT_DOWN) {
            if (robot == null) {
                System.out.println("Thread: " + Thread.currentThread().getName() + " all robots " + robotType + " are down already");
                return;
            }
            robot = robotQueue.poll();
            executeTask(task, robot);
        }

        if (task.actionType == Task.Action.DO_WORK) {
            if (robot == null || robot.isBusy()) {
                robot = RobotFactory.createRobotWithManager(robotType);
            } else {
                robot = robotQueue.poll();
            }
            executeTask(task, robot);
            pushBackToMap(robot);
        }
    }

    private void assignTaskForAllRobots(Task task) {
        System.out.println("Thread: " + Thread.currentThread().getName() + ", assign task to all robots " + task.actionType);

        availableRobotMap.forEach((robotType, robotQueue) -> {
            if (task.actionType == Task.Action.DO_WORK) {
                if (robotQueue.isEmpty()) {
                    System.out.println("Thread: " + Thread.currentThread().getName() + ", no robots available --> creating robot");
                    var robot = RobotFactory.createRobotWithManager(robotType);
                    pushBackToMap(robot);
                }
                robotQueue.forEach(robot -> executeTask(task, robot));
            }

            if (task.actionType == Task.Action.SHUT_DOWN) {
                if (robotQueue.isEmpty()) {
                    System.out.println("Thread: " + Thread.currentThread().getName() + " all robots " + robotType + " are down already");
                    return;
                }
                List<Robot> tmp = new ArrayList<>();
                robotQueue.drainTo(tmp);
                tmp.forEach(robot -> executeTask(task, robot));
            }
        });
    }

    private void executeTask(Task task, Robot robot) {
        switch (task.actionType) {
            case Task.Action.DO_WORK -> robot.doWork(task);
            case Task.Action.SHUT_DOWN -> robot.shutDown();
        }
    }
}
