package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.BroadcastTaskDto;
import org.example.dto.SingleTaskDto;
import org.example.model.Task;
import org.example.service.LogService;
import org.example.service.Manager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final LogService logService = LogService.getInstance();
    private final Manager manager;

    public TaskController(Manager manager) {
        this.manager = manager;
    }

    @PostMapping("/tasks/broadcast")
    public void broadcast(@Valid @RequestBody BroadcastTaskDto dto) {
        Task task;
        switch (dto.getActionType()) {
            case DO_WORK -> task = Task.workForAll(dto.getPayload());
            case SHUT_DOWN -> task = Task.shutDownForAll();
            default -> throw new IllegalArgumentException("actionType type " + dto.getActionType() + " not supported");
        }
        manager.addTask(task);
        logService.log("Thread: %s, Broadcast task submitted %s".formatted(Thread.currentThread().getName(), task));
    }

    @PostMapping("/tasks/single")
    public void single(@Valid @RequestBody SingleTaskDto dto) {
        Task task;
        switch (dto.getActionType()) {
            case DO_WORK -> task = Task.work(dto.getRobotType(), dto.getPayload());
            case SHUT_DOWN -> task = Task.shutDown(dto.getRobotType());
            default -> throw new IllegalArgumentException("actionType type " + dto.getActionType() + " not supported");
        }
        manager.addTask(task);
        logService.log("Thread: %s, Single task submitted %s".formatted(Thread.currentThread().getName(), task));
    }

    @GetMapping("/logs/stream")
    public SseEmitter streamLogs() {
        return logService.subscribe();
    }
}
