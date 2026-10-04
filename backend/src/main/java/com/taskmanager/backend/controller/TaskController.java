package com.taskmanager.backend.controller;

import com.taskmanager.backend.dto.OnCreate;
import com.taskmanager.backend.dto.TaskRequestDto;
import com.taskmanager.backend.dto.TaskResponseDto;
import com.taskmanager.backend.security.AuthenticatedUser;
import com.taskmanager.backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponseDto> createTask(@Validated(OnCreate.class) @RequestBody TaskRequestDto request,
                                                      @AuthenticationPrincipal AuthenticatedUser user) {
        TaskResponseDto created = taskService.createTask(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/my")
    public ResponseEntity<List<TaskResponseDto>> getMyTasks(@AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(taskService.getMyTasks(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable UUID id,
                                                       @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(taskService.getTaskById(id, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable UUID id,
                                                      @Valid @RequestBody TaskRequestDto request,
                                                      @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(taskService.updateTask(id, request, user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}