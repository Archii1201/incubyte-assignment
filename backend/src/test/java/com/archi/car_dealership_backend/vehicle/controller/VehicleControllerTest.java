package com.archi.car_dealership_backend.vehicle.controller;

import com.archi.car_dealership_backend.auth.util.JwtAuthenticationFilter;
import com.archi.car_dealership_backend.security.CustomUserDetailsService;
import com.archi.car_dealership_backend.vehicle.dto.VehicleResponse;
import com.archi.car_dealership_backend.vehicle.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.archi.car_dealership_backend.config.SecurityConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
@Import(SecurityConfig.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createVehicle_returns201_whenSuccessful() throws Exception {

        VehicleResponse response =
                new VehicleResponse(
                        UUID.randomUUID(),
                        "Toyota",
                        "Corolla",
                        "Sedan",
                        new BigDecimal("22000"),
                        5,
                        "ACTIVE"
                );

        when(vehicleService.createVehicle(any()))
                .thenReturn(response);

        String json = """
                {
                  "make":"Toyota",
                  "model":"Corolla",
                  "category":"Sedan",
                  "price":22000,
                  "quantity":5
                }
                """;

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.make")
                        .value("Toyota"));
    }
}