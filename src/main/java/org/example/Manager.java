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
            if (rt == Robot.Type.UNIVERSAL) continue;
            availableRobotMap.put(rt, new LinkedBlockingQueue<>());
        }
    }

    public boolean hasTask() {
        return !taskQueue.isEmpty();
    }

    public void addTaskToQueue(Task task) {
        taskQueue.add(task);
    }

    public void addRobotToMap(Robot robot) {
        var robotQueue = availableRobotMap.computeIfAbsent(robot.getType(), k -> new LinkedBlockingQueue<>());
        robotQueue.add(robot);
    }

    public void manageWork() {
        var task = taskQueue.poll();
        var robotType = task.robotType;
        
        if (robotType == Robot.Type.UNIVERSAL) {
            List<Robot> tmp = new ArrayList<>();
            availableRobotMap.forEach((rt, rq) -> rq.drainTo(tmp));

            if (tmp.isEmpty()) {
                System.out.println("NO ROBOTS AVAILABLE --> CREATING ROBOT");
                var robot = RobotFactory.createRobotWithManager(robotType, this);
                tmp.add(robot);
            }
            tmp.forEach(robot -> executeTask(task, robot));
            return;
        }

        var robot = availableRobotMap.get(robotType).poll();
        if (robot == null) {
            robot = RobotFactory.createRobotWithManager(robotType, this);
        }
        executeTask(task, robot);
    }

    public void notifyTaskCompleted(Robot robot) {
        addRobotToMap(robot);
    }

    private void executeTask(Task task, Robot robot) {
        switch (task.taskType) {
            case Task.Type.DO_WORK -> robot.doWork(task);
            case Task.Type.SHUT_DOWN -> robot.shutDown();
        }
    }
}
