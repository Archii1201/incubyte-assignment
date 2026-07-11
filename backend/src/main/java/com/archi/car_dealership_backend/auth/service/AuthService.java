package com.archi.car_dealership_backend.auth.service;

import com.archi.car_dealership_backend.auth.dto.AuthResponse;
import com.archi.car_dealership_backend.auth.dto.LoginRequest;
import com.archi.car_dealership_backend.auth.dto.RegisterRequest;
import com.archi.car_dealership_backend.auth.exception.DuplicateResourceException;
import com.archi.car_dealership_backend.auth.util.JwtUtil;
import com.archi.car_dealership_backend.entity.Role;
import com.archi.car_dealership_backend.entity.User;
import com.archi.car_dealership_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException(
                    "Email already registered"
            );
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(
                        passwordEncoder.encode(request.password())
                )
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);

        String token = jwtUtil.generateToken(saved);

        return new AuthResponse(
                token,
                saved.getEmail(),
                saved.getRole().name()
        );
    }
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.email(),

                        request.password()
                )
        );

        User user = userRepository.findByEmail(

                request.email()

        ).orElseThrow();

        String token = jwtUtil.generateToken(user);

        return new AuthResponse(

                token,

                user.getEmail(),

                user.getRole().name()
        );
    }
}
