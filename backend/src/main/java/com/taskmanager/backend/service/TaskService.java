package com.taskmanager.backend.service;

import com.taskmanager.backend.dto.TaskRequestDto;
import com.taskmanager.backend.dto.TaskResponseDto;
import com.taskmanager.backend.entity.Task;
import com.taskmanager.backend.exception.TaskNotFoundException;
import com.taskmanager.backend.repository.TaskRepository;
import com.taskmanager.backend.security.AuthenticatedUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponseDto createTask(TaskRequestDto request, AuthenticatedUser creator) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : "TODO");
        task.setOwnerId(request.getOwnerId() != null ? request.getOwnerId() : creator.getId());
        task.setCreatedAt(OffsetDateTime.now());
        task.setUpdatedAt(OffsetDateTime.now());

        Task saved = taskRepository.save(task);
        return toResponseDto(saved);
    }

    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private TaskResponseDto toResponseDto(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getOwnerId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public TaskResponseDto getTaskById(UUID id, AuthenticatedUser user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if (!"ADMIN".equals(user.getRole()) && !task.getOwnerId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to view this task");
        }
        return toResponseDto(task);
    }

    public List<TaskResponseDto> getMyTasks(UUID ownerId) {
        return taskRepository.findByOwnerId(ownerId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public TaskResponseDto updateTask(UUID id, TaskRequestDto request, AuthenticatedUser user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        boolean isAdmin = "ADMIN".equals(user.getRole());
        boolean isOwner = task.getOwnerId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You do not have permission to update this task");
        }

        if (isAdmin) {
            if (request.getTitle() != null) task.setTitle(request.getTitle());
            if (request.getDescription() != null) task.setDescription(request.getDescription());
            if (request.getStatus() != null) task.setStatus(request.getStatus());
        } else {
            // Owning USER: status only — title/description in the request are silently ignored
            if (request.getStatus() != null) task.setStatus(request.getStatus());
        }

        task.setUpdatedAt(OffsetDateTime.now());
        Task saved = taskRepository.save(task);
        return toResponseDto(saved);
    }

    public void deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}