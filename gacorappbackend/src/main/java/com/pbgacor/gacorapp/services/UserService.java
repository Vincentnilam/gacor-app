package com.pbgacor.gacorapp.services;

import com.pbgacor.gacorapp.repositories.UserRepository;
import com.pbgacor.gacorapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User registerUser(User user) {

        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already in use");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // initial state for all registrations
        user.setActive(false);
        user.setRole("ROLE_USER");
        User savedUser = userRepository.save(user);

        // send pending approval email to user
        emailService.sendPendingApprovalEmail(savedUser.getEmail());
        // send required activation to admin?
        emailService.sendAdminNotificationEmail(savedUser.getUsername(), savedUser.getEmail());
        // should I surround it wif a try n catch in case email sending failed?

        return savedUser;
    }
}
