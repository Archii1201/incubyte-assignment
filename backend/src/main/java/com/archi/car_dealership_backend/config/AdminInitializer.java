package com.archi.car_dealership_backend.config;

import com.archi.car_dealership_backend.entity.Role;
import com.archi.car_dealership_backend.entity.User;
import com.archi.car_dealership_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.existsByRole(Role.ADMIN)) {
            return;
        }

        User admin = User.builder()
                .name("Administrator")
                .email("admin@cardealership.com")
                .password(passwordEncoder.encode("Admin@123"))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);

        System.out.println("========================================");
        System.out.println(" Default administrator created");
        System.out.println(" Email    : admin@cardealership.com");
        System.out.println(" Password : Admin@123");
        System.out.println("========================================");
    }
}