package com.bricool.security.service_pack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

//    public void save(com.bricool.security.service_pack.Service service) {
//        serviceRepository.save(service);
//    }

    public void save(com.bricool.security.service_pack.Service service) throws IOException {
        // Check if an image is provided
        String base64Image = service.getImageBase64(); // Assuming there is a getImageBase64() method in your Service class
        if (base64Image != null && !base64Image.isEmpty()) {
            // Decode base64 image data
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // Specify the path where images will be stored
            String uploadPath = "C:\\Users\\HP\\Desktop\\bricoole\\Spring_boot_bricoole\\spring-boot-3-jwt-security\\src\\main\\resources\\serviceImages";

            // Create a unique file name (you can customize this logic)
            String fileName = System.currentTimeMillis() + "_photo.jpg";

            // Create the file path
            Path filePath = Paths.get(uploadPath, fileName);

            // Write the image bytes to the file
            java.nio.file.Files.write(filePath, imageBytes);

            // Set the imagePath in the entity
            service.setImagePath(fileName);
        }

        serviceRepository.save(service);
    }


    public List<com.bricool.security.service_pack.Service> getAllServices() throws IOException {
        List<com.bricool.security.service_pack.Service> services = serviceRepository.findAll();

        // Specify the path where images are stored
        String imagePath = "C:\\Users\\HP\\Desktop\\bricoole\\Spring_boot_bricoole\\spring-boot-3-jwt-security\\src\\main\\resources\\serviceImages";

        // For each service, load the image, encode it to Base64, and set it in imageBase64 field
        return services.stream()
                .peek(service -> {
                    try {
                        String imageName = service.getImagePath();
                        if (imageName != null) {
                            Path imagePathToFile = Paths.get(imagePath, imageName);
                            byte[] imageBytes = Files.readAllBytes(imagePathToFile);
                            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                            service.setImageBase64(base64Image);
                        }
                    } catch (IOException e) {
                        e.printStackTrace(); // Handle the exception according to your needs
                    }
                })
                .collect(Collectors.toList());
    }
}
