package com.bricool.security.chat;


import com.bricool.security.user.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final ConversationService conversationService;
    private final MessageService messageService;

    @Autowired
    public MessageController(ConversationService conversationService, MessageService messageService) {
        this.conversationService = conversationService;
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public void sendMessage(
            @AuthenticationPrincipal User client,
            @RequestBody MessageRequest messageRequest
    ) {
        // Get or create conversation between client and service provider
        Conversation conversation = conversationService.getOrCreateConversation(messageRequest.getClientId(), messageRequest.getServiceProviderId(), messageRequest.getServiceId());
        if (messageRequest.getClientId() == null || messageRequest.getServiceProviderId() == null || messageRequest.getServiceId() == null) {
            throw new IllegalArgumentException("Invalid arguments in Controller: clientId, serviceProviderId, or serviceId is null");
        }
        System.out.println("client id"+ messageRequest.getClientId());
        System.out.println("service provider"+messageRequest.getServiceProviderId());
        System.out.println("service id is "+messageRequest.getServiceId());
        // Create a new message
        Message message = Message.builder()
                .conversation(conversation)
                .user(client)
                .content(messageRequest.getContent())
                .timestamp(LocalDateTime.now())
                .client_id( messageRequest.getClientId())
                .build();

        // Save the message to the database
        messageService.saveMessage(message);
    }
//@PostMapping("/send")
//public void sendMessage(@RequestBody MessageRequest messageRequest) {
//    // Get or create conversation between client and service provider
//    Conversation conversation = conversationService.getOrCreateConversation(
//            messageRequest.getClientId(),
//            messageRequest.getServiceProviderId(),
//            messageRequest.getServiceId()
//    );
//    System.out.println("client id: " + messageRequest.getClientId());
//    System.out.println("service provider: " + messageRequest.getServiceProviderId());
//    System.out.println("service id is: " + messageRequest.getServiceId());
//
//    // Create a new message
//    Message message = Message.builder()
//            .conversation(conversation)
//            .user(null) // Assuming you don't need the user object here
//            .content(messageRequest.getContent())
//            .timestamp(LocalDateTime.now())
//            .build();
//
//    // Save the message to the database
//    messageService.saveMessage(message);
//}


    @Data
    private static class MessageRequest {
        private Integer serviceProviderId;
        private Integer serviceId;
        private String content;
        private Integer clientId;
    }

// return message
    @GetMapping("/conversation/{conversationId}")
    public List<Message> getMessagesByConversationId(@PathVariable Integer conversationId) {
        // Get messages by conversation ID
        return messageService.getMessagesByConversationId(conversationId);
    }
// get list of conversations
    @GetMapping("/conversations")
    public List<Conversation> getUserConversations() {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        // Pass the user to the service to fetch conversations
        return conversationService.getUserConversations(currentUser);
    }
////make messages as read when opening a conversation
    @PostMapping("/markAsRead/{conversationId}")
    public void markMessagesAsRead(@PathVariable Integer conversationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Integer userId = currentUser.getId();

        messageService.markMessagesAsRead(conversationId, userId);
    }
}
