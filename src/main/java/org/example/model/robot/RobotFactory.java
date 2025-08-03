package org.example.model.robot;

import org.example.service.LogService;

public interface RobotFactory {
    LogService logService = LogService.getInstance();

    static Robot createRobotWithManager(Robot.Type type) {
        logService.log("Thread: %s, create robot %s".formatted(Thread.currentThread().getName(), type));
        switch (type) {
            case TERMINATOR -> {
                return new RobotTerminator();
            }
            case CLEANER -> {
                return new RobotCleaner();
            }
            default -> {
                var msg = "Robot type %s not supported".formatted(type);
                logService.log(msg);
                throw new IllegalArgumentException(msg);
            }
        }
    }
}
