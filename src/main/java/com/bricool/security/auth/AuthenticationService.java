package com.bricool.security.auth;

import com.bricool.security.config.JwtService;
import com.bricool.security.token.Token;
import com.bricool.security.token.TokenRepository;
import com.bricool.security.token.TokenType;
import com.bricool.security.user.User;
import com.bricool.security.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

//  public AuthenticationResponse register(RegisterRequest request) {
//    var user = User.builder()
//        .firstname(request.getFirstname())
//        .lastname(request.getLastname())
//        .email(request.getEmail())
//        .password(passwordEncoder.encode(request.getPassword()))
//        .role(request.getRole())
//        .build();
//    var savedUser = repository.save(user);
//    var jwtToken = jwtService.generateToken(user);
//    var refreshToken = jwtService.generateRefreshToken(user);
//    saveUserToken(savedUser, jwtToken);
//    return AuthenticationResponse.builder()
//        .accessToken(jwtToken)
//            .refreshToken(refreshToken)
//        .build();
//  }
public AuthenticationResponse register(RegisterRequest request) {

  var user = User.builder()
          .firstname(request.getFirstname())
          .lastname(request.getLastname())
          .email(request.getEmail())
          .password(passwordEncoder.encode(request.getPassword()))
          .country(request.getCountry())  // Set country
          .city(request.getCity())  // Set city
          .address(request.getAddress())  // Set address
          .about(request.getAbout())
          .job(request.getJob())
          .build();
  String imagePath = saveUserImage(user, request);
  // Set the imagePath to the user's filePath attribute
  if (imagePath != null) {
    user.setImagePath(imagePath);
  }
  var savedUser = repository.save(user);
  var jwtToken = jwtService.generateToken(user);
  var refreshToken = jwtService.generateRefreshToken(user);
  saveUserToken(savedUser, jwtToken);
  return AuthenticationResponse.builder()
          .accessToken(jwtToken)
          .refreshToken(refreshToken)
          .build();
}

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }


  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

// set image
private String saveUserImage(User user, RegisterRequest request) {
  // Check if an image is provided
  String base64Image = request.getImageBase64();
  if (base64Image != null && !base64Image.isEmpty()) {
    // Decode base64 image data
    byte[] imageBytes = Base64.getDecoder().decode(base64Image);
    String uploadDir = "C:\\Users\\HP\\Desktop\\bricoole\\Spring_boot_bricoole\\spring-boot-3-jwt-security\\src\\main\\resources\\serviceImages\\profileImages";
    // Create a unique file name (you can customize this logic)
    String fileName = System.currentTimeMillis() + "_user_photo.jpg";

    // Create the file path
    Path filePath = Paths.get(uploadDir, fileName);

    // Write the image bytes to the file
    try {
      java.nio.file.Files.write(filePath, imageBytes);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Set the imagePath in the entity
    System.out.println("imagePath in save method is :"+fileName);

    // Set other fields like location


    // Return the imagePath
    return fileName;
  }

  // Return null if no image was provided
  return null;
}


}
