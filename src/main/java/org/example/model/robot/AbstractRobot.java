package org.example.model.robot;

import org.example.model.Task;
import org.example.service.LogService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractRobot implements Robot {
    private static int counter = 0;

    private final LogService logService = LogService.getInstance();
    private final Robot.Type type;
    private final String compositeKey;
    private final long workingTime;
    private final ExecutorService singleThreadExecutor;
    private volatile boolean isBusy;

    protected AbstractRobot(Robot.Type type, long workingTime) {
        this.type = type;
        this.compositeKey = "%s-%d".formatted(type, ++counter);
        this.workingTime = workingTime;
        this.singleThreadExecutor = Executors.newSingleThreadExecutor(runnable -> new Thread(runnable, compositeKey));
    }

    @Override
    public void doWork(Task task) {
        singleThreadExecutor.submit(() -> {
            isBusy = true;
            logService.log("Thread: %s, task is: %s".formatted(Thread.currentThread().getName(), task.payload));
            sleep(workingTime);
            isBusy = false;
        });
    }

    @Override
    public void shutDown() {
        singleThreadExecutor.submit(() -> {
            isBusy = true;
            logService.log("Thread: %s, do shutdown".formatted(Thread.currentThread().getName()));
            sleep(workingTime);
            isBusy = false;
        });
        singleThreadExecutor.shutdown();
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getCompositeKey() {
        return compositeKey;
    }

    @Override
    public boolean isBusy() {
        return isBusy;
    }

    protected void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}