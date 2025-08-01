package org.example.robot;

public interface RobotFactory {
    static Robot create(Robot.Type type) {
        switch (type) {
            case Robot.Type.TERMINATOR -> {
                return new RobotTerminator();
            }
            case Robot.Type.CLEANER -> {
                return new RobotCleaner();
            }
            case Robot.Type.UNIVERSAL -> {
                return new RobotTerminator(); // TODO: return random robot
            }
            default -> throw new IllegalArgumentException("Robot type not supported: " + type);
        }
    }
}
