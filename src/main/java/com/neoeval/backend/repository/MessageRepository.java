package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderId(Long senderId); // Obtener mensajes enviados por un remitente
    List<Message> findByRecipientId(Long recipientId); // Obtener mensajes recibidos por un destinatario

    // ✅ CORRECCIÓN: Usamos 'SentAt' en lugar de 'SentDate' para el ordenamiento.
    // El método está bien formado y requiere 4 parámetros de ID, tal como lo estabas usando en el servicio.
    List<Message> findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderBySentAtAsc(
            Long user1Id, // Caso 1: Sender = user1, Recipient = user2
            Long user2Id,
            Long user3Id, // Caso 2: Sender = user2, Recipient = user1
            Long user4Id
    );
}