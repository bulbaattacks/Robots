package org.example;

import org.example.robot.Robot;
import org.example.robot.RobotCleaner;
import org.example.robot.RobotTerminator;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        var terminator1 = new RobotTerminator();
        var terminator2 = new RobotTerminator();
        var cleaner1 = new RobotCleaner();
        var cleaner2 = new RobotCleaner();

        var task1 = Task.work(Robot.Type.TERMINATOR, "kill humanity");
        var task2 = Task.work(Robot.Type.CLEANER, "clean the mess");

        var task3 = Task.shutDown(Robot.Type.CLEANER);
        var task4 = Task.shutDown(Robot.Type.TERMINATOR);

        List<Robot> robotList = List.of(terminator1, terminator2, cleaner1, cleaner2);
        List<Task> taskList = List.of(task1, task2, task3, task4);

        var manager = new Manager();

        for (var robot : robotList) {
            manager.addRobotToMap(robot);
        }

        for (var task : taskList) {
            manager.addTaskToQueue(task);
        }

        while (manager.hasTask()) {
            manager.doTask();
        }

    }
}
