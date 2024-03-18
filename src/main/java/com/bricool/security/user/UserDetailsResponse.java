package com.bricool.security.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Data
@Builder
@AllArgsConstructor
public class UserDetailsResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
//new code after update
    private String imagePath;
    private String about;
    private String job;
    private String country;
    private String city;
    private String address;
    private String imageBase64;
//    public static UserDetailsResponse fromUser(User user) {
//        return UserDetailsResponse.builder()
//                .id(user.getId())
//                .firstname(user.getFirstname())
//                .lastname(user.getLastname())
//                .email(user.getEmail())
//                .role(user.getRole())
//                .about(user.getAbout())
//                .job(user.getJob())
//                .country(user.getCountry())
//                .city(user.getCity())
//                .address(user.getAddress())
//                .build();
//    }

    public static UserDetailsResponse fromUser(User user) {
        UserDetailsResponse userDetailsResponse = UserDetailsResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .role(user.getRole())
                .about(user.getAbout())
                .job(user.getJob())
                .country(user.getCountry())
                .city(user.getCity())
                .address(user.getAddress())
                .imageBase64(user.getImageBase64())
                .build();

        // Specify the path where user images are stored
        String imagePath = "C:\\Users\\HP\\Desktop\\bricoole\\Spring_boot_bricoole\\spring-boot-3-jwt-security\\src\\main\\resources\\serviceImages\\profileImages";

        try {
            String imageName = user.getImagePath();
            if (imageName != null) {
                Path imagePathToFile = Paths.get(imagePath, imageName);
                byte[] imageBytes = Files.readAllBytes(imagePathToFile);
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                userDetailsResponse.setImageBase64(base64Image);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return userDetailsResponse;
    }


}
