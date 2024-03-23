package com.bricool.security.service_pack;


import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service")
public class MyService {

    @Id
    @GeneratedValue
    private Integer id;

    private String serviceName;
    private String description;
    private LocalDate date;
    private BigDecimal price;
    private String category;
    private String location;
    @Column(name = "service_provider_id")
    private Integer serviceProvider;
    @Getter
    private String imagePath;

    @Getter
    @Transient // Ignore this field for database persistence
    private String imageBase64;


    public MyService(Integer id, String serviceName, String description, LocalDate date, BigDecimal price, String category, String location, Integer serviceProvider, String imagePath) {
        this.id = id;
        this.serviceName = serviceName;
        this.description = description;
        this.date = date;
        this.price = price;
        this.category = category;
        this.location = location;
        this.serviceProvider = serviceProvider;
        this.imagePath = imagePath;

        // Generate Base64 image if imagePath is not null
        if (imagePath != null) {
            try {
                this.imageBase64 = generateBase64Image(imagePath);
                System.out.println("imageBase64 is"+  this.imageBase64 );
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception according to your needs
            }
        }
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String generateBase64Image(String imageName) throws IOException {
        // Specify the path where images are stored
        String imagePath = "C:\\Users\\HP\\Desktop\\bricoole\\Spring_boot_bricoole\\spring-boot-3-jwt-security\\src\\main\\resources\\serviceImages";

        // Construct the path to the image file
        Path imagePathToFile = Paths.get(imagePath, imageName);

        // Read the image bytes from the file
        byte[] imageBytes = Files.readAllBytes(imagePathToFile);

        // Encode the image bytes to Base64
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}

