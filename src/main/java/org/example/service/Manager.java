package org.example.service;

import org.example.model.Task;
import org.example.model.robot.Robot;
import org.example.model.robot.RobotFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class Manager {
    private final LogService logService = LogService.getInstance();

    private final Map<Robot.Type, BlockingQueue<Robot>> availableRobotMap = new EnumMap<>(Robot.Type.class);
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
        var robotQueue = availableRobotMap.get(robotType);
        logService.log("Thread: %s,  task for %s robot --> %s".formatted(Thread.currentThread().getName(), robotType, task.actionType));

        var robot = robotQueue.peek();

        if (task.actionType == Task.Action.SHUT_DOWN) {
            if (robot == null) {
                logService.log("Thread: %s, all robots %s are down already".formatted(Thread.currentThread().getName(), robotType));
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
        logService.log("Thread: %s, assign task to all robots, %s".formatted(Thread.currentThread().getName(), task.actionType));

        availableRobotMap.forEach((robotType, robotQueue) -> {
            if (task.actionType == Task.Action.DO_WORK) {
                if (robotQueue.isEmpty()) {
                    logService.log("Thread: %s, , no robots available --> creating robot".formatted(Thread.currentThread().getName()));
                    var robot = RobotFactory.createRobotWithManager(robotType);
                    pushBackToMap(robot);
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
}
