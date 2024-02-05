package com.bricool.security.service_pack;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

    @PostMapping
    public ResponseEntity<String> createService(@RequestBody Service service) {
        // Call the save method in ServiceService to insert the service into the database
        serviceService.save(service);

        // Return a response indicating success
        return new ResponseEntity<>("Service created successfully", HttpStatus.CREATED);
    }
}
