package com.archi.car_dealership_backend.vehicle.controller;

import com.archi.car_dealership_backend.auth.util.JwtAuthenticationFilter;
import com.archi.car_dealership_backend.auth.util.JwtUtil;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    @Autowired private MockMvc mockMvc;
    @MockitoBean private VehicleService vehicleService;
    @MockitoBean private JwtUtil jwtUtil; // needed because SecurityConfig wires the filter
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;
    @Test
    @WithMockUser(roles = "ADMIN")
    void createVehicle_returns201_onSuccess() throws Exception {
        VehicleResponse response = new VehicleResponse(UUID.randomUUID(), "Toyota", "Corolla",
                "Sedan", new BigDecimal("22000.00"), 5, "ACTIVE");
        when(vehicleService.createVehicle(any())).thenReturn(response);

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"make":"Toyota","model":"Corolla","category":"Sedan","price":22000.00,"quantity":5}
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.make").value("Toyota"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createVehicle_returns400_onNegativePrice() throws Exception {
        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"make":"Toyota","model":"Corolla","category":"Sedan","price":-500,"quantity":5}
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createVehicle_returns400_onNegativeQuantity() throws Exception {
        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"make":"Toyota","model":"Corolla","category":"Sedan","price":22000,"quantity":-2}
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    void createVehicle_returns403_forNonAdmin() throws Exception {
        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"make":"Toyota","model":"Corolla","category":"Sedan","price":22000,"quantity":5}
                    """))
                .andExpect(status().isForbidden());
    }

    @Test
    void createVehicle_returns401_withoutAuth() throws Exception {
        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"make":"Toyota","model":"Corolla","category":"Sedan","price":22000,"quantity":5}
                    """))
                .andExpect(status().isUnauthorized());
    }
}