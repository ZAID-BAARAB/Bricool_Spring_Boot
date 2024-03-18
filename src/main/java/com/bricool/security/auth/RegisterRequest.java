package com.bricool.security.auth;

import com.bricool.security.user.Role;
import jakarta.persistence.Transient;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private Role role;

  // Additional fields
  private String imagePath;
  private String country;
  private String city;
  private String address;
  private String about;
  private String job;
  @Getter
  @Transient // Ignore this field for database persistence
  private String imageBase64;
  // Getters and setters for the new fields
}
