package com.archi.car_dealership_backend.auth.util;

import com.archi.car_dealership_backend.entity.Role;
import com.archi.car_dealership_backend.entity.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {
    private JwtUtil jwtUtil = new JwtUtil("thisIsMySuperSecretJwtKeyForCarDealership2026Secure", 86400000);

    @Test
    void generateAndValidateToken_roundTrip() {
        User user = User.builder().email("archi@test.com").role(Role.USER).build();
        String token = jwtUtil.generateToken(user);

        assertThat(jwtUtil.isTokenValid(token)).isTrue();
        assertThat(jwtUtil.extractEmail(token)).isEqualTo("archi@test.com");
    }


    @Test
    void isTokenValid_returnsFalse_forExpiredToken() {
        JwtUtil shortLived = new JwtUtil("test-secret-key-must-be-long-enough-for-hs256", -1000);
        User user = User.builder().email("archi@test.com").role(Role.USER).build();
        String token = shortLived.generateToken(user);

        assertThat(shortLived.isTokenValid(token)).isFalse();
    }
}
