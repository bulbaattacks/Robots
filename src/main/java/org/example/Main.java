package org.example;

import org.example.robot.Robot;
import org.example.robot.RobotFactory;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        var task1 = Task.work(Robot.Type.TERMINATOR, "kill humanity");
        var task2 = Task.work(Robot.Type.CLEANER, "clean the mess");
        var task3 = Task.shutDown(Robot.Type.CLEANER);
        var task4 = Task.shutDown(Robot.Type.TERMINATOR);

        List<Task> taskList = List.of(task1, task2, task3, task4);

        var manager = new Manager();

        for (var task : taskList) {
            manager.addTaskToQueue(task);
            var robot = RobotFactory.create(task.robotType);
            manager.addRobotToMap(robot);
        }

        while (manager.hasTask()) {
            manager.doTask();
        }

    }
}
