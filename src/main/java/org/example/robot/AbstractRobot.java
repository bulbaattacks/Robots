package org.example.robot;

import org.example.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractRobot implements Robot {
    private static int counter = 0;

    private final ExecutorService singleThreadExecutor;
    private volatile boolean isBusy;
    private final long workingTime;

    protected final Robot.Type type;
    protected final int id;

    protected AbstractRobot(Robot.Type type, long workingTime) {
        this.type = type;
        this.id = ++counter;
        this.workingTime = workingTime;
        this.singleThreadExecutor = Executors.newSingleThreadExecutor(runnable -> new Thread(runnable, type + "-" + id));
    }

    @Override
    public void doWork(Task task) {
        singleThreadExecutor.submit(() -> {
            isBusy = true;
            System.out.println("Thread: " + Thread.currentThread().getName() + ", task is: " + task.payload);
            sleep(workingTime);
            isBusy = false;
        });
    }

    @Override
    public void shutDown() {
        singleThreadExecutor.submit(() -> {
            isBusy = true;
            System.out.println("Thread: " + Thread.currentThread().getName() + ", do shutdown");
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