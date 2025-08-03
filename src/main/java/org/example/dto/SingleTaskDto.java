package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.model.Task;
import org.example.model.robot.Robot;

public class SingleTaskDto {
    @NotNull
    private Task.Action actionType;
    @NotNull
    private Robot.Type robotType;
    @Size(min = 0, max = 100, message = "Expected length 100 symbols max")
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
