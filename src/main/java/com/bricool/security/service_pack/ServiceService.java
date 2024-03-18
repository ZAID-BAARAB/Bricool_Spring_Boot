package com.bricool.security.service_pack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.bricool.security.user.CustomUserDetails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    public void save(MyService service) throws IOException {
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


    public List<MyService> getAllServices() throws IOException {
        List<MyService> services = serviceRepository.findAll();

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

    public List<MyService> getServicesByCategory(String category) {
        // Implementation to retrieve services by category from the repository
        List<MyService> services = serviceRepository.findByCategory(category);

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

    //find by id method

    public List<MyService> getServiceById(Integer id) {
        // Implementation to retrieve services by category from the repository
        Optional<MyService> services = serviceRepository.findById(id);

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

 //delete Service by ServiceId and UserID
//    public void deleteService(Integer serviceId, Integer userId) throws ChangeSetPersister.NotFoundException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            // Replace CustomUserDetails with the actual name of your custom interface or class
//            if (authentication.getPrincipal() instanceof CustomUserDetails) {
//                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//
//                // Now you can access information about the logged-in user
//                Integer currentUserId = customUserDetails.getId();
//                System.out.println("the current user id is "+currentUserId);
//                System.out.println("the sender ID is "+userId);
//                if (Objects.equals(currentUserId, userId)) {
//                    Optional<com.bricool.security.service_pack.Service> serviceToDelete = serviceRepository.findByIdAndServiceProvider(serviceId, userId);
//                    if (serviceToDelete.isPresent()) {
//                        serviceRepository.delete(serviceToDelete.get());
//                    } else {
//                        throw new ChangeSetPersister.NotFoundException();
//                    }
//                }
//            }
//        } else {
//           throw new ChangeSetPersister.NotFoundException();
//        }
//    }

    public void deleteService(Integer serviceId, Integer userId) throws ChangeSetPersister.NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

                Integer currentUserId = customUserDetails.getId();
                System.out.println("the current user id is " + currentUserId);
                System.out.println("the sender ID is " + userId);

                if (Objects.equals(currentUserId, userId)) {
                    Optional<MyService> serviceToDelete = serviceRepository.findByIdAndServiceProvider(serviceId, userId);

                    if (serviceToDelete.isPresent()) {
                        // Delete the associated image from the static folder
                        deleteImage(serviceToDelete.get().getImagePath());

                        // Delete the service from the database
                        serviceRepository.delete(serviceToDelete.get());
                    } else {
                        throw new ChangeSetPersister.NotFoundException();
                    }
                }
            }
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }




    private void deleteImage(String imagePath) {
        try {
            Path path = Paths.get("C:\\Users\\HP\\Desktop\\bricoole\\Spring_boot_bricoole\\spring-boot-3-jwt-security\\src\\main\\resources\\serviceImages\\" + imagePath);
            System.out.println("Deleting file: " + path);

            if (Files.exists(path)) {
                Files.deleteIfExists(path);
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("File not found: " + path);
            }
        } catch (IOException e) {
            // Handle the exception (e.g., log it)
            e.printStackTrace();
        }
    }




}
