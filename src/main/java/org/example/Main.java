package org.example;

import org.example.robot.Robot;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var manager = new Manager();

        manager.addTask(Task.work(Robot.Type.TERMINATOR, "..KILL..KILL..KILL.."));

        manager.addTask(Task.shutDown(Robot.Type.TERMINATOR));
        manager.addTask(Task.shutDown(Robot.Type.TERMINATOR));

        manager.addTask(Task.work(Robot.Type.TERMINATOR, "..KILL..KILL..KILL.."));

        manager.addTask(Task.shutDown(Robot.Type.TERMINATOR));


        manager.addTask(Task.work(Robot.Type.CLEANER, "..CLEAN..CLEAN..CLEAN.."));
        manager.addTask(Task.shutDown(Robot.Type.CLEANER));

        manager.addTask(Task.workForAll("..TASK..FOR..ALL.."));
        manager.addTask(Task.shutDownForAll());
        manager.addTask(Task.shutDownForAll());
        manager.addTask(Task.shutDownForAll());
        manager.addTask(Task.shutDownForAll());
        manager.addTask(Task.shutDownForAll());
        manager.addTask(Task.shutDownForAll());
        manager.addTask(Task.shutDownForAll());
        manager.addTask(Task.shutDownForAll());

        while (manager.hasTask()) {
            Thread.sleep(500);
            manager.manageWork();
        }
    }
}
