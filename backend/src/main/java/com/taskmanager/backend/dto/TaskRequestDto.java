package com.taskmanager.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;

public class TaskRequestDto {

    private UUID ownerId; // Optional. ADMIN can assign the task to a specific user by their UUID.
    // If omitted, the task defaults to being owned by the creating ADMIN.

    @NotBlank(message = "Title is required", groups = OnCreate.class)
    private String title;


    private String description;

    @Pattern(regexp = "TODO|IN_PROGRESS|DONE", message = "Status must be TODO, IN_PROGRESS, or DONE")
    private String status;

    public TaskRequestDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}