package org.example.robot;

import org.example.Manager;
import org.example.Task;

import java.util.UUID;

public abstract class AbstractRobot implements Robot {
    private final Manager manager;
    protected final Robot.Type type;
    protected final UUID id;

    protected AbstractRobot(Manager manager, Robot.Type type) {
        this.manager = manager;
        this.type = type;
        this.id = UUID.randomUUID();
    }

    @Override
    public void doWork(Task task) {
        System.out.println(getType() + " id: " + id + " DO WORK " + task.payload);
        manager.notifyTaskCompleted(this);
    }

    @Override
    public void shutDown() {
        System.out.println(getType() + " id: " + id + " SHUTDOWN");
    }

    @Override
    public Type getType() {
        return type;
    }
}
