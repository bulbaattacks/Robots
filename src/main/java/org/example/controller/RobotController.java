package org.example.controller;

import org.example.service.Manager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class RobotController {

    private final Manager manager;

    public RobotController(Manager manager) {
        this.manager = manager;
    }

    @GetMapping("/robots")
    public Set<String> getAvailableRobots() {
        return manager.getAvailableRobots();
    }
}
