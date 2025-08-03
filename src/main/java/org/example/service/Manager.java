package org.example.service;

import org.example.model.Task;
import org.example.model.robot.Robot;
import org.example.model.robot.RobotFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class Manager {
    private final LogService logService = LogService.getInstance();

    private final Map<Robot.Type, BlockingQueue<Robot>> robots = new EnumMap<>(Robot.Type.class);
    private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private final Map<String, Robot> availableRobotMap = new ConcurrentHashMap<>();

    public Manager() {
        for (var rt : Robot.Type.values()) {
            robots.put(rt, new LinkedBlockingQueue<>());
        }
    }

    public Set<String> getAvailableRobots() {
        var  availableRobots = availableRobotMap.keySet();
        logService.log("Available robots: %s".formatted(availableRobots));
        return availableRobots;
    }

    public boolean hasTask() {
        return !taskQueue.isEmpty();
    }

    public void addTask(Task task) {
        taskQueue.add(task);
    }

    public void pushToRobotMap(Robot robot) {
        robots
                .computeIfAbsent(robot.getType(), k -> new LinkedBlockingQueue<>())
                .add(robot);
        availableRobotMap.put(robot.getCompositeKey(), robot);
    }

    @Scheduled(fixedDelay = 500)
    private void manageWork() {
        if (!hasTask()) {
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
        var robotQueue = robots.get(robotType);
        logService.log("Thread: %s,  task for %s robot --> %s".formatted(Thread.currentThread().getName(), robotType, task.actionType));

        var robot = robotQueue.peek();

        if (task.actionType == Task.Action.SHUT_DOWN) {
            if (robot == null) {
                logService.log("Thread: %s, all robots %s are down already".formatted(Thread.currentThread().getName(), robotType));
                return;
            }
            robot = robotQueue.poll();
            deleteFromAvailableRobotMap(robot.getCompositeKey());
            executeTask(task, robot);
        }

        if (task.actionType == Task.Action.DO_WORK) {
            if (robot == null || robot.isBusy()) {
                robot = RobotFactory.createRobotWithManager(robotType);
            } else {
                robot = robotQueue.poll();
            }
            executeTask(task, robot);
            pushToRobotMap(robot);
        }
    }

    private void assignTaskForAllRobots(Task task) {
        logService.log("Thread: %s, assign task to all robots, %s".formatted(Thread.currentThread().getName(), task.actionType));

        robots.forEach((robotType, robotQueue) -> {
            if (task.actionType == Task.Action.DO_WORK) {
                if (robotQueue.isEmpty()) {
                    logService.log("Thread: %s, , no robots available --> creating robot".formatted(Thread.currentThread().getName()));
                    var robot = RobotFactory.createRobotWithManager(robotType);
                    pushToRobotMap(robot);
                }
                robotQueue.forEach(robot -> executeTask(task, robot));
            }

            if (task.actionType == Task.Action.SHUT_DOWN) {
                if (robotQueue.isEmpty()) {
                    logService.log("Thread: %s, all robots %s are down already".formatted(Thread.currentThread().getName(), robotType));
                    return;
                }
                List<Robot> tmp = new ArrayList<>();
                robotQueue.drainTo(tmp);
                tmp.forEach(robot -> deleteFromAvailableRobotMap(robot.getCompositeKey()));
                tmp.forEach(robot -> executeTask(task, robot));
            }
        });
    }

    private void executeTask(Task task, Robot robot) {
        switch (task.actionType) {
            case DO_WORK -> robot.doWork(task);
            case SHUT_DOWN -> robot.shutDown();
        }
    }

    private void deleteFromAvailableRobotMap(String key) {
        availableRobotMap.remove(key);
    }
}
