package com.taskmanager.backend.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class TaskResponseDto {

    private UUID id;
    private String title;
    private String description;
    private String status;
    private UUID ownerId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public TaskResponseDto() {
    }

    public TaskResponseDto(UUID id, String title, String description, String status,
                           UUID ownerId, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}