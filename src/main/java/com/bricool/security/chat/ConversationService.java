package com.bricool.security.chat;

import com.bricool.security.service_pack.MyService;
import com.bricool.security.service_pack.ServiceRepository;
import com.bricool.security.user.User;
import com.bricool.security.user.UserRepository;
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
    private final UserRepository userRepository;
    private final ServiceRepository myServiceRepository;
    
    @Autowired
    public ConversationService(ConversationRepository conversationRepository,
                               UserRepository userRepository,
                               ServiceRepository myServiceRepository) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.myServiceRepository = myServiceRepository;
    }
//    public Conversation getOrCreateConversation(Integer clientId, Long serviceProviderId, Integer serviceId) {
//        Optional<Conversation> existingConversation = conversationRepository.findByClient_IdAndServiceProvider_Id(Math.toIntExact(clientId), serviceProviderId);
//
//        return existingConversation.orElseGet(() -> createConversation(clientId, serviceProviderId, serviceId));
//    }

    public Conversation getOrCreateConversation(Integer clientId, Integer  serviceProviderId, Integer serviceId) {
        if (clientId == null || serviceProviderId == null || serviceId == null) {
            throw new IllegalArgumentException("Invalid arguments in getOrCreateConversation: clientId, serviceProviderId, or serviceId is null");
        }
        // Check if there's an existing conversation involving both the client and service provider with the given serviceId

        Optional<Conversation> existingConversation = conversationRepository.findByClient_IdAndServiceProvider_IdAndService_Id(
                clientId, serviceProviderId, serviceId);
        if(existingConversation.isPresent()){
            System.out.println("conversation already exist");
        }
        return existingConversation.orElseGet(() -> createConversation(clientId, serviceProviderId, serviceId));
    }


//    private Conversation createConversation(Integer clientId, Long serviceProviderId, Integer serviceId) {
//
//        User client = new User();
//        client.setId(clientId);
//
//        User serviceProvider = new User();
//        serviceProvider.setId(Math.toIntExact(serviceProviderId));
//
//        MyService service = new MyService(); // Assuming you have a Service class
//        service.setId(serviceId);
//
//        Conversation newConversation = Conversation.builder()
//                .client(client)
//                .serviceProvider(serviceProvider)
//                .service(service) // Associate the conversation with the service
//                .build();
//
//        return conversationRepository.save(newConversation);
//    }

    private Conversation createConversation(Integer clientId, Integer serviceProviderId, Integer serviceId) {
        if (clientId == null || serviceProviderId == null || serviceId == null) {
            throw new IllegalArgumentException("Invalid arguments in createConversation: clientId, serviceProviderId, or serviceId is null");
        }
        // Fetch the client and service provider from the database
        User client = userRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found"));
        User serviceProvider = userRepository.findById(serviceProviderId).orElseThrow(() -> new IllegalArgumentException("Service provider not found"));

        // Fetch the service from the database
        MyService service = myServiceRepository.findById(serviceId).orElseThrow(() -> new IllegalArgumentException("Service not found"));

        // Create the conversation object
        Conversation newConversation = Conversation.builder()
                .client(client)
                .serviceProvider(serviceProvider)
                .service(service)
                .build();

        return conversationRepository.save(newConversation);
    }


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
