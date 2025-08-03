package org.example.robot;

import org.example.Task;

public abstract class AbstractRobot implements Robot {
    private static int counter = 0;

    private boolean isBusy;
    private final long workingTime;

    protected final Robot.Type type;
    protected final int id;

    protected AbstractRobot(Robot.Type type, long workingTime) {
        this.type = type;
        this.id = ++counter;
        this.workingTime = workingTime;
    }

    @Override
    public void doWork(Task task) {
        isBusy = true;
        System.out.println("Thread: " + Thread.currentThread().getName() + ", " + getType() + " id: " + id + ", task is: " + task.payload);
        sleep(workingTime);
        isBusy = false;
    }

    @Override
    public void shutDown() {
        isBusy = true;
        System.out.println("Thread: " + Thread.currentThread().getName() + ", " + getType() + " id: " + id + ", do shutdown");
        sleep(workingTime);
        isBusy = false;
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