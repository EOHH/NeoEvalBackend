package com.neoeval.backend.dto.response;

import java.time.Instant; // ✅ Importado Instant

public class MessageResponse {
    private Long id;
    private String content;
    private Instant sentDate; // ✅ Actualizado a Instant
    private boolean read;
    private Long senderId;
    private String senderName;
    private Long recipientId;
    private String recipientName;

    // Constructor vacío
    public MessageResponse() {
    }

    // Constructor con todos los campos
    public MessageResponse(Long id, String content, Instant sentDate, boolean read,
                           Long senderId, String senderName, Long recipientId, String recipientName) {
        this.id = id;
        this.content = content;
        this.sentDate = sentDate;
        this.read = read;
        this.senderId = senderId;
        this.senderName = senderName;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Instant getSentDate() { return sentDate; }
    public void setSentDate(Instant sentDate) { this.sentDate = sentDate; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
}