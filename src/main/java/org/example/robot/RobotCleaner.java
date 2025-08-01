package org.example.robot;

import org.example.Manager;

public class RobotCleaner extends AbstractRobot {

    public RobotCleaner(Manager manager) {
        super(manager, Robot.Type.CLEANER);
    }
}
