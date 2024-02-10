package com.bricool.security.service_pack;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class ServiceController {
    private final ServiceService serviceService;

    @PostMapping
    public ResponseEntity<String> createService(@RequestBody Service service) throws IOException {
        // Call the save method in ServiceService to insert the service into the database
        serviceService.save(service);

        // Return a response indicating success
        return new ResponseEntity<>("Service created successfully", HttpStatus.CREATED);
    }
}
