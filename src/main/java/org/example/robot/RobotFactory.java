package org.example.robot;

import org.example.Manager;

public interface RobotFactory {
    static Robot createRobotWithManager(Robot.Type type, Manager manager) {
        switch (type) {
            case Robot.Type.TERMINATOR -> {
                return new RobotTerminator(manager);
            }
            case Robot.Type.CLEANER -> {
                return new RobotCleaner(manager);
            }
            case Robot.Type.UNIVERSAL -> {
                return new RobotTerminator(manager); // TODO: return random robot
            }
            default -> throw new IllegalArgumentException("Robot type not supported: " + type);
        }
    }
}
