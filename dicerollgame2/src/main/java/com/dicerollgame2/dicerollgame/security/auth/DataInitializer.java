package com.dicerollgame2.dicerollgame.security.auth;

import com.dicerollgame2.dicerollgame.security.user.Role;
import com.dicerollgame2.dicerollgame.security.user.User;
import com.dicerollgame2.dicerollgame.security.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.findByEmail("admin@admin.com").isPresent()) {
            // Creates an admin user if it doesn't exist
            User admin = User.builder()
                    .firstname("Admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("12345678"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}
