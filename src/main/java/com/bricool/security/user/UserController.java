package com.bricool.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

//get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsResponse> getUserDetailsById(@PathVariable Integer userId) {
        UserDetailsResponse userDetails = service.getUserDetailsById(userId);
        return ResponseEntity.ok(userDetails);
    }
}
