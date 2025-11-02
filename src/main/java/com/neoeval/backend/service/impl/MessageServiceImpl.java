package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.MessageRequest;
import com.neoeval.backend.dto.response.MessageResponse;
import com.neoeval.backend.entity.Message;
import com.neoeval.backend.entity.User;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.MessageRepository;
import com.neoeval.backend.repository.UserRepository;
import com.neoeval.backend.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant; // ✅ Importar java.time.Instant
import java.util.List;
// import java.util.Date; // 🛑 Eliminar importación innecesaria (new Date())
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public MessageResponse sendMessage(MessageRequest messageRequest) {
        User sender = userRepository.findById(messageRequest.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", messageRequest.getSenderId()));

        User recipient = userRepository.findById(messageRequest.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", messageRequest.getRecipientId()));

        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setSender(sender);
        message.setRecipient(recipient);

        // 🛑 Corregido: El constructor de Message ya establece 'sentAt = Instant.now()',
        // pero si quieres sobrescribirlo o asegurarte, usa setSentAt().
        // message.setSentAt(Instant.now()); // Opcional, ya está en el constructor
        message.setRead(false);

        Message savedMessage = messageRepository.save(message);
        return mapToMessageResponse(savedMessage);
    }

    @Override
    public MessageResponse getMessageById(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje", "id", id));
        return mapToMessageResponse(message);
    }

    @Override
    public List<MessageResponse> getSentMessages(Long userId) {
        List<Message> messages = messageRepository.findBySenderId(userId);
        return messages.stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponse> getReceivedMessages(Long userId) {
        List<Message> messages = messageRepository.findByRecipientId(userId);
        return messages.stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageResponse markAsRead(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje", "id", id));
        message.setRead(true);
        Message updatedMessage = messageRepository.save(message);
        return mapToMessageResponse(updatedMessage);
    }

    @Override
    public List<MessageResponse> getConversation(Long user1Id, Long user2Id) {
        // Asumiendo que MessageRepository tiene el método:
        // findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderBySentAtAsc
        List<Message> conversation = messageRepository.findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderBySentAtAsc(user1Id, user2Id, user1Id, user2Id);
        return conversation.stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());
    }

    private MessageResponse mapToMessageResponse(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setContent(message.getContent());

        // ✅ CORRECCIÓN: Usar getSentAt() de la Entidad. Asumo que MessageResponse usa Instant.
        // Si MessageResponse usa Instant, no se necesita conversión.
        response.setSentDate(message.getSentAt());

        response.setRead(message.isRead());

        if (message.getSender() != null) {
            response.setSenderId(message.getSender().getId());
            response.setSenderName(message.getSender().getName());
        } else {
            response.setSenderId(null);
            response.setSenderName("Unknown Sender");
        }

        if (message.getRecipient() != null) {
            response.setRecipientId(message.getRecipient().getId());
            response.setRecipientName(message.getRecipient().getName());
        } else {
            response.setRecipientId(null);
            response.setRecipientName("Unknown Recipient");
        }
        return response;
    }
}