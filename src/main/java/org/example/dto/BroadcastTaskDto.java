package org.example.dto;

import org.example.model.Task;

public class BroadcastTaskDto {
    private Task.Action actionType;
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
