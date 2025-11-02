package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.MessageRequest;
import com.neoeval.backend.dto.response.MessageResponse;

import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(MessageRequest messageRequest);
    MessageResponse getMessageById(Long id); // Confirmado y declarado aquí

    // Los nombres de estos métodos deben coincidir con lo que usa el controlador
    List<MessageResponse> getSentMessages(Long userId); // Renombrado de getMessagesBySenderId
    List<MessageResponse> getReceivedMessages(Long userId); // Renombrado de getMessagesByRecipientId
    MessageResponse markAsRead(Long id); // Método para marcar como leído
    List<MessageResponse> getConversation(Long user1Id, Long user2Id); // Método para la conversación
}