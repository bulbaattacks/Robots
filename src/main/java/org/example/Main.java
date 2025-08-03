package org.example;

import org.example.robot.Robot;

public class Main {
    public static void main(String[] args) {
        var manager = new Manager();

        manager.addTaskToQueue(Task.work(Robot.Type.TERMINATOR, "..KILL..KILL..KILL.."));
        manager.addTaskToQueue(Task.shutDown(Robot.Type.TERMINATOR));

        manager.addTaskToQueue(Task.work(Robot.Type.CLEANER, "..CLEAN..CLEAN..CLEAN.."));
        manager.addTaskToQueue(Task.shutDown(Robot.Type.CLEANER));

        manager.addTaskToQueue(Task.workForAll("..TASK..FOR..ALL.."));
        manager.addTaskToQueue(Task.shutDownForAll());

        while (manager.hasTask()) {
            manager.manageWork();
        }
    }
}
