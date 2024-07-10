package com.bricool.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

//get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsResponse> getUserDetailsById(@PathVariable Integer userId) {
        UserDetailsResponse userDetails = userService.getUserDetailsById(userId);
        return ResponseEntity.ok(userDetails);
    }

//Edit User Informations
@PutMapping("/{userId}")
public User editUser(@PathVariable Integer userId, @RequestBody User updatedUserDetails) {
    // Retrieve the existing user from the database
    User existingUser = userService.getUserById(userId);

    if (existingUser == null) {
        // Handle the case when the user doesn't exist
        // You can throw an exception or return a specific response
    } else {
        // Update the existing user entity with the new details
        if (updatedUserDetails.getFirstname() != null) {
            existingUser.setFirstname(updatedUserDetails.getFirstname());
        }
        if (updatedUserDetails.getLastname() != null) {
            existingUser.setLastname(updatedUserDetails.getLastname());
        }
        if (updatedUserDetails.getEmail() != null) {
            existingUser.setEmail(updatedUserDetails.getEmail());
        }
        if (updatedUserDetails.getImagePath() != null) {
            existingUser.setImagePath(updatedUserDetails.getImagePath());
        }
        if (updatedUserDetails.getAbout() != null) {
            existingUser.setAbout(updatedUserDetails.getAbout());
        }
        if (updatedUserDetails.getJob() != null) {
            existingUser.setJob(updatedUserDetails.getJob());
        }
        if (updatedUserDetails.getCountry() != null) {
            existingUser.setCountry(updatedUserDetails.getCountry());
        }
        if (updatedUserDetails.getCity() != null) {
            existingUser.setCity(updatedUserDetails.getCity());
        }
        if (updatedUserDetails.getAddress() != null) {
            existingUser.setAddress(updatedUserDetails.getAddress());
        }
        // Update other properties similarly
    }

    // Save the updated user entity back to the database
    User editedUser = userService.saveUser(existingUser);

    return editedUser;
}
}
