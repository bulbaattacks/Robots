package org.example.robot;

public interface RobotFactory {
    static Robot createRobotWithManager(Robot.Type type) {
        System.out.println("Thread: " + Thread.currentThread().getName() + ", create robot " + type);
        switch (type) {
            case Robot.Type.TERMINATOR -> {
                return new RobotTerminator();
            }
            case Robot.Type.CLEANER -> {
                return new RobotCleaner();
            }
            default -> throw new IllegalArgumentException("Robot type " + type + " not supported" );
        }
    }
}
