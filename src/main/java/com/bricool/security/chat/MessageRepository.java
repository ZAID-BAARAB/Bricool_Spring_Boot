package com.bricool.security.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversation_IdOrderByTimestamp(Integer conversationId);

    // You can add custom query methods here if needed
}
