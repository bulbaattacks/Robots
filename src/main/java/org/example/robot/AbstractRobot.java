package org.example.robot;

import org.example.Task;

import java.util.UUID;

public abstract class AbstractRobot implements Robot {
    protected final Robot.Type type;
    protected final UUID id;

    protected AbstractRobot(Robot.Type type) {
        this.type = type;
        this.id = UUID.randomUUID();
    }

    @Override
    public void doWork(Task task) {
        System.out.println(getType() + " id: " + id + " DO WORK " + task.payload);
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
