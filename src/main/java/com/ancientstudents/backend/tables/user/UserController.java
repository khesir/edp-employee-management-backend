package com.ancientstudents.backend.tables.user;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ancientstudents.backend.exception.UserNotFoundException;

import java.security.Principal;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    @Autowired
    private final UserService service;

    @Autowired
    private UserRepository userRepository;

    @PatchMapping("/user/change-password")
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/user/email", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByEmail(@RequestParam(value = "args") String email) {
        if (email == null) {
            return ResponseEntity.badRequest().build(); // Bad request if email parameter is null
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if user not found
        }

        User user = userOptional.get(); // Extracting the user from Optional
        return ResponseEntity.ok(user); // Return user if found
    }
}
