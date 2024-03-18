package com.bricool.security.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }
// return a conversation by ID
    public List<Message> getMessagesByConversationId(Integer conversationId) {
        // Implement the logic to retrieve messages by conversation ID
        return messageRepository.findByConversation_IdOrderByTimestamp(conversationId);
    }
}
