package com.archi.car_dealership_backend.auth.controller;

import com.archi.car_dealership_backend.auth.dto.AuthResponse;
import com.archi.car_dealership_backend.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AuthService authService;

    @Test
    void register_returns201_whenSuccessful() throws Exception {

        when(authService.register(any()))
                .thenReturn(
                        new AuthResponse(
                                "jwt-token",
                                "archi@test.com",
                                "USER"
                        )
                );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name":"Archi",
                          "email":"archi@test.com",
                          "password":"password123"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token")
                        .value("jwt-token"));
    }
    @Test
    void register_returns400_whenEmailInvalid() throws Exception {

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                       "name":"Archi",
                       "email":"wrong-email",
                       "password":"password123"
                    }
                    """))
                .andExpect(status().isBadRequest());
    }
}
