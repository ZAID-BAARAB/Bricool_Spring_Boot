package com.bricool.security.service_pack;

import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class ServiceController {
    private final ServiceService serviceService;
//    private final UserService service;  i commented this for ssurther use in getting user by ID

    @PostMapping
    public ResponseEntity<String> createService(@RequestBody MyService service) throws IOException {
        // Call the save method in ServiceService to insert the service into the database
        serviceService.save(service);

        // Return a response indicating success
        return new ResponseEntity<>("Service created successfully", HttpStatus.CREATED);
    }
//  returning all services
    @GetMapping
    public ResponseEntity<List<MyService>> getAllServices() throws IOException {
        // Call the getAllServices method in ServiceService to retrieve all services
        List<MyService> services = serviceService.getAllServices();

        // Return the list of services with a success status
        return new ResponseEntity<>(services, HttpStatus.OK);
    }
// Finding services by category

    @GetMapping("/byCategory")
    public ResponseEntity<List<MyService>> getServicesByCategory(@RequestParam String category) throws IOException {
        // Call the getServicesByCategory method in ServiceService to retrieve services by category
        List<MyService> services = serviceService.getServicesByCategory(category);

        if (services.isEmpty()) {
            // If no services are found for the given category, return a not found status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Return the list of services with a success status
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

// get 4 by category
//@GetMapping("/SuggestionbyCategory")
//public ResponseEntity<List<MyService>> getSuggestionServicesByCategory(@RequestParam String category) throws IOException {
//    // Call the getServicesByCategory method in ServiceService to retrieve services by category
//    List<MyService> services = serviceService.getLimitedServicesByCategory(category, 4);
//
//    if (services.isEmpty()) {
//        // If no services are found for the given category, return a not found status
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    // Return the list of services with a success status
//    return new ResponseEntity<>(services, HttpStatus.OK);
//}

    @GetMapping("/SuggestionbyCategory/{category}")
    public ResponseEntity<List<MyService>> getSuggestionServicesByCategory(@PathVariable String category) throws IOException {
        // Call the getServicesByCategory method in ServiceService to retrieve services by category
        List<MyService> services = serviceService.getLimitedServicesByCategory(category, 4);

        if (services.isEmpty()) {
            // If no services are found for the given category, return a not found status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Return the list of services with a success status
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

// get service by id

    @GetMapping("/{id}")
    public ResponseEntity<List<MyService>> getServiceById(@PathVariable Long id) throws IOException {
        // Call the getServiceById method in ServiceService to retrieve the service(s) by its ID
        List<MyService> services = serviceService.getServiceById(Math.toIntExact(id));

        if (services.isEmpty()) {
            // If no service is found for the given ID, return a not found status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Return the service(s) with a success status
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteService(@RequestBody Map<String, Integer> requestBody) {
        Integer serviceId = requestBody.get("serviceId");
        Integer userId = requestBody.get("userId");

        if (serviceId == null || userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request format");
        }

        try {
            serviceService.deleteService(serviceId, userId);
            return ResponseEntity.ok("Service deleted successfully");
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/current")
    public String someMethod() {
        // Get the authentication object from the SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        String username = null;
        if (authentication != null && authentication.isAuthenticated()) {
            // Get the principal (which should be an instance of UserDetails)
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Now you can access information about the logged-in user
            username = userDetails.getUsername();
            return username;
        } else {
            username= "this did not work";
        }
        return username;
    }

    //get user by ID    commented it because i have it in UserController
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<UserDetailsResponse> getUserDetailsById(@PathVariable Integer userId) {
//        System.out.println(userId);
//        UserDetailsResponse userDetails = service.getUserDetailsById(userId);
//        return ResponseEntity.ok(userDetails);
//    }
        //DTO
//    @GetMapping("/v2/services/{category}")
//    public List<ServiceDTO> getServicesByCategoryV2(@PathVariable String category) {
//        return serviceService.getServicesByCategoryV2(category);
//    }


//    @GetMapping("/user/{serviceId}")
//    public ResponseEntity<User> getUserByServiceId(@PathVariable Long serviceId) {
//        User user = serviceService.getUserByServiceId(serviceId);
//        if (user != null) {
//            return ResponseEntity.ok(user);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

//return Random services
    @GetMapping("/randomservices")
    public List<MyService> getRandomServices() {
        return serviceService.getRandomServices(6);
    }
// get service by service provider id
    @GetMapping("/provider/{serviceProviderId}")
    public ResponseEntity<List<MyService>> getServicesByServiceProvider(@PathVariable Integer serviceProviderId) {
        List<MyService> services = serviceService.getServicesByServiceProvider(serviceProviderId);
        return ResponseEntity.ok(services);
    }

}
