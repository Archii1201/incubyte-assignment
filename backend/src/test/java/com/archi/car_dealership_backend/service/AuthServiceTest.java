package com.archi.car_dealership_backend.service;

import com.archi.car_dealership_backend.entity.User;
import com.archi.car_dealership_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    @Test
    void register_success_savesHashedPasswordAndReturnsToken() {
        RegisterRequest request = new RegisterRequest("Archi", "archi@test.com", "password123");
        when(userRepository.existsByEmail("archi@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed_pw");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtUtil.generateToken(any(User.class))).thenReturn("mock-jwt");

        AuthResponse response = authService.register(request);

        assertThat(response.token()).isEqualTo("mock-jwt");
        verify(userRepository).save(argThat(u -> u.getPassword().equals("hashed_pw")));
    }

    @Test
    void register_throwsDuplicateResourceException_whenEmailExists() {
        RegisterRequest request = new RegisterRequest("Archi", "dup@test.com", "password123");
        when(userRepository.existsByEmail("dup@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already registered");

        verify(userRepository, never()).save(any());
    }
}
