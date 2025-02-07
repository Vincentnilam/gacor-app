package com.pbgacor.gacorapp.controllers;

import com.pbgacor.gacorapp.models.User;
import com.pbgacor.gacorapp.repositories.UserRepository;
import com.pbgacor.gacorapp.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;

    private final EmailService emailService;

    public AdminController(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @PutMapping("/activate/{userId}")
    public ResponseEntity<?> activateUser(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setActive(true);
            userRepository.save(foundUser);

            // send success email to user
            emailService.sendAccountActivatedEmail(foundUser.getEmail());

            return ResponseEntity.ok("User activated and email sent");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}
