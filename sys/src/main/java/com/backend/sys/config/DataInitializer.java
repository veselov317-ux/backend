package com.backend.sys.config;

import com.backend.sys.entity.Role;
import com.backend.sys.entity.User;
import com.backend.sys.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedDemoUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            createUserIfMissing(userRepository, passwordEncoder, "admin@servicedesk.local", "Admin User", Role.ADMIN);
            createUserIfMissing(userRepository, passwordEncoder, "agent@servicedesk.local", "Support Agent", Role.AGENT);
            createUserIfMissing(userRepository, passwordEncoder, "user@servicedesk.local", "Demo Requester", Role.USER);
        };
    }

    private void createUserIfMissing(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String email,
            String fullName,
            Role role
    ) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            return;
        }
        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
    }
}
