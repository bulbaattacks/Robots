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
                int idx = (int) (Math.random() * Robot.Type.values().length);
                var rt = Robot.Type.values()[idx];
                if (rt == Robot.Type.UNIVERSAL) {
                    rt = Robot.Type.values()[idx-1];
                }
                System.out.println("hello from createRobotWithManager " + rt);
                return createRobotWithManager(rt, manager);
            }
            default -> throw new IllegalArgumentException("Robot type not supported: " + type);
        }
    }
}
