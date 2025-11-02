package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MessageRequest {
    @NotBlank(message = "Message content is required")
    @Size(max = 2000, message = "Message content cannot exceed 2000 characters")
    private String content;

    @NotNull(message = "Sender ID is required") // <-- Añadido
    private Long senderId; // <-- Añadido

    @NotNull(message = "Recipient ID is required")
    private Long recipientId;

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSenderId() { // <-- Añadido
        return senderId;
    }

    public void setSenderId(Long senderId) { // <-- Añadido
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
}