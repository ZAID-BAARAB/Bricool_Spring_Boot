package com.bricool.security.chat;

import com.bricool.security.service_pack.MyService;
import com.bricool.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
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

//    public List<Conversation> getUserConversations(User user) {
//        return conversationRepository.findByClientOrServiceProvider(user, user);
//    }

    public List<Conversation> getUserConversations(User user) {
        List<Conversation> conversations = conversationRepository.findByClientOrServiceProvider(user, user);

        // Populate imageBase64 for each MyService associated with the conversations
        for (Conversation conversation : conversations) {
            MyService service = conversation.getService();
            if (service != null) {
                String imagePath = service.getImagePath();
                if (imagePath != null) {
                    try {
                        // Generate and set imageBase64 for the service
                        String imageBase64 = generateBase64Image(imagePath);
                        service.setImageBase64(imageBase64);
                    } catch (IOException e) {
                        e.printStackTrace(); // Handle the exception according to your needs
                    }
                }
            }
        }

        return conversations;
    }

    private String generateBase64Image(String imagePath) throws IOException {
        // Specify the path where images are stored
        String basePath = "C:\\Users\\HP\\Desktop\\bricoole\\Spring_boot_bricoole\\spring-boot-3-jwt-security\\src\\main\\resources\\serviceImages";

        // Construct the path to the image file
        Path imagePathToFile = Paths.get(basePath, imagePath);

        // Read the image bytes from the file
        byte[] imageBytes = Files.readAllBytes(imagePathToFile);

        // Encode the image bytes to Base64
        return Base64.getEncoder().encodeToString(imageBytes);
    }
    // Add additional methods as needed for retrieving or manipulating conversations
}
