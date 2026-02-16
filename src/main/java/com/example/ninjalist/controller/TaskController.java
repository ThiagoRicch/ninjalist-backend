package com.example.ninjalist.controller;

import com.example.ninjalist.config.JWTUserData;
import com.example.ninjalist.model.TaskModel;
import com.example.ninjalist.model.UserModel;
import com.example.ninjalist.repository.UserRepository;
import com.example.ninjalist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService,  UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    @PostMapping("/me")
    public TaskModel create(@Valid @RequestBody TaskModel taskModel,
                            @AuthenticationPrincipal JWTUserData jwtUserData) {
        UserModel user = userRepository.findById(jwtUserData.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        taskModel.setUser(user);
        return taskService.createTask(taskModel);
    }

    @GetMapping("/me")
    public List<TaskModel> list(@AuthenticationPrincipal JWTUserData jwtUserData) {
        return taskService.listTaskByUser(jwtUserData.userId());
    }

    @PutMapping("/{id}")
    public TaskModel update(@Valid @PathVariable Long id,
                            @RequestBody TaskModel taskModel) {
        return taskService.updateTask(id, taskModel);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}

