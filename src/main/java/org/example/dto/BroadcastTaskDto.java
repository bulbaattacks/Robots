package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.model.Task;

public class BroadcastTaskDto {
    @NotNull
    private Task.Action actionType;
    @Size(min = 0, max = 100, message = "Expected length 100 symbols max")
    private String payload;

    public Task.Action getActionType() {
        return actionType;
    }

    public void setActionType(Task.Action actionType) {
        this.actionType = actionType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
