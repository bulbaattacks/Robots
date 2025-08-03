package org.example.dto;

import org.example.model.Task;
import org.example.model.robot.Robot;

public class SingleTaskDto {
    private Task.Action actionType;
    private Robot.Type robotType;
    private String payload;

    public Task.Action getActionType() {
        return actionType;
    }

    public void setActionType(Task.Action actionType) {
        this.actionType = actionType;
    }

    public Robot.Type getRobotType() {
        return robotType;
    }

    public void setRobotType(Robot.Type robotType) {
        this.robotType = robotType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
