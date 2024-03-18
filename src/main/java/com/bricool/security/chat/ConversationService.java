package com.bricool.security.chat;

import com.bricool.security.service_pack.MyService;
import com.bricool.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }
    public Conversation getOrCreateConversation(Integer clientId, Long serviceProviderId, Integer serviceId) {
        Optional<Conversation> existingConversation = conversationRepository.findByClient_IdAndServiceProvider_Id(Math.toIntExact(clientId), serviceProviderId);

        return existingConversation.orElseGet(() -> createConversation(clientId, serviceProviderId, serviceId));
    }

    private Conversation createConversation(Integer clientId, Long serviceProviderId, Integer serviceId) {
        User client = new User();
        client.setId(clientId);

        User serviceProvider = new User();
        serviceProvider.setId(Math.toIntExact(serviceProviderId));

        MyService service = new MyService(); // Assuming you have a Service class
        service.setId(serviceId);

        Conversation newConversation = Conversation.builder()
                .client(client)
                .serviceProvider(serviceProvider)
                .service(service) // Associate the conversation with the service
                .build();

        return conversationRepository.save(newConversation);
    }

    public List<Conversation> getUserConversations(User user) {
        return conversationRepository.findByClientOrServiceProvider(user, user);
    }
    // Add additional methods as needed for retrieving or manipulating conversations
}
