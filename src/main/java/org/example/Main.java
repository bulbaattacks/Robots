package org.example;

import org.example.robot.Robot;
import org.example.robot.RobotCleaner;
import org.example.robot.RobotTerminator;

public class Main {

    public static void main(String[] args) {
        var terminator1 = new RobotTerminator();
        var cleaner1 = new RobotCleaner();

        var task1 = Task.work(Robot.Type.TERMINATOR, "kill humanity");
        var task2 = Task.work(Robot.Type.CLEANER, "clean the mess");

        var task3 = Task.shutDown(Robot.Type.CLEANER);
        var task4 = Task.shutDown(Robot.Type.TERMINATOR);

        terminator1.doWork(task1);
        cleaner1.doWork(task2);

        terminator1.shutDown();
        cleaner1.shutDown();
    }
}
