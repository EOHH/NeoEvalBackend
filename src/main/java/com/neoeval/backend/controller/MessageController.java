package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.MessageRequest;
import com.neoeval.backend.dto.response.MessageResponse;
import com.neoeval.backend.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus; // Importar HttpStatus para ResponseEntity.status
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importar PreAuthorize
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping // POST /api/messages - Enviar un mensaje
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'PARENT', 'ADMIN')") // Todos los roles que pueden enviar mensajes
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest messageRequest) {
        MessageResponse response = messageService.sendMessage(messageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Retorna 201 Created
    }

    @GetMapping("/{id}") // GET /api/messages/{id} - Obtener un mensaje por ID
    // Restringir el acceso para que solo el emisor o receptor del mensaje puedan verlo, o un ADMIN
    @PreAuthorize("hasAnyRole('ADMIN') or (@messageService.getMessageById(#id).senderId == authentication.principal.id) or (@messageService.getMessageById(#id).recipientId == authentication.principal.id)")
    public ResponseEntity<MessageResponse> getMessageById(@PathVariable Long id) {
        MessageResponse response = messageService.getMessageById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sent/{userId}") // GET /api/messages/sent/{userId} - Obtener mensajes enviados por un usuario
    @PreAuthorize("hasAnyRole('ADMIN') or (#userId == authentication.principal.id)") // Solo el usuario o un admin puede ver sus mensajes enviados
    public ResponseEntity<List<MessageResponse>> getSentMessages(@PathVariable Long userId) {
        // El nombre del método en el servicio ahora coincide: getSentMessages
        List<MessageResponse> responses = messageService.getSentMessages(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/received/{userId}") // GET /api/messages/received/{userId} - Obtener mensajes recibidos por un usuario
    @PreAuthorize("hasAnyRole('ADMIN') or (#userId == authentication.principal.id)") // Solo el usuario o un admin puede ver sus mensajes recibidos
    public ResponseEntity<List<MessageResponse>> getReceivedMessages(@PathVariable Long userId) {
        // El nombre del método en el servicio ahora coincide: getReceivedMessages
        List<MessageResponse> responses = messageService.getReceivedMessages(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/read") // PUT /api/messages/{id}/read - Marcar un mensaje como leído
    @PreAuthorize("hasAnyRole('ADMIN') or (@messageService.getMessageById(#id).recipientId == authentication.principal.id)") // Solo el receptor o un admin puede marcarlo como leído
    public ResponseEntity<MessageResponse> markAsRead(@PathVariable Long id) {
        // El nombre del método en el servicio ahora coincide: markAsRead
        MessageResponse response = messageService.markAsRead(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversation/{user1Id}/{user2Id}") // GET /api/messages/conversation/{user1Id}/{user2Id} - Obtener conversación entre dos usuarios
    @PreAuthorize("hasAnyRole('ADMIN') or (#user1Id == authentication.principal.id) or (#user2Id == authentication.principal.id)") // Cualquiera de los dos usuarios o un admin puede ver la conversación
    public ResponseEntity<List<MessageResponse>> getConversation(
            @PathVariable Long user1Id,
            @PathVariable Long user2Id) {
        // El nombre del método en el servicio ahora coincide: getConversation
        List<MessageResponse> responses = messageService.getConversation(user1Id, user2Id);
        return ResponseEntity.ok(responses);
    }
}