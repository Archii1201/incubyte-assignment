package com.archi.car_dealership_backend.auth.util;

import com.archi.car_dealership_backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    public String generateToken(User user) {
        return "dummy-token";
    }

    public Object isTokenValid(String token) {
    }

    public Object extractEmail(String token) {
    }
}
