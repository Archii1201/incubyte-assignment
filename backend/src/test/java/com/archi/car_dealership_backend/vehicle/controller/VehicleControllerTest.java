package com.archi.car_dealership_backend.vehicle.controller;

import com.archi.car_dealership_backend.auth.exception.ResourceNotFoundException;
import com.archi.car_dealership_backend.auth.util.JwtAuthenticationFilter;
import com.archi.car_dealership_backend.auth.util.JwtUtil;
import com.archi.car_dealership_backend.security.CustomUserDetailsService;
import com.archi.car_dealership_backend.security.JwtAccessDeniedHandler;
import com.archi.car_dealership_backend.security.JwtAuthenticationEntryPoint;
import com.archi.car_dealership_backend.vehicle.dto.VehicleResponse;
import com.archi.car_dealership_backend.vehicle.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.archi.car_dealership_backend.config.SecurityConfig;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
@WebMvcTest(VehicleController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(SecurityConfig.class)
class VehicleControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private VehicleService vehicleService;
    @MockitoBean private JwtUtil jwtUtil; // needed because SecurityConfig wires the filter
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;
    @MockitoBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockitoBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Test
    @WithMockUser
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
    @WithMockUser(roles = "USER")
    void createVehicle_returns201_forAuthenticatedUser() throws Exception {

        VehicleResponse response =
                new VehicleResponse(
                        UUID.randomUUID(),
                        "Toyota",
                        "Corolla",
                        "Sedan",
                        new BigDecimal("22000.00"),
                        5,
                        "ACTIVE"
                );

        when(vehicleService.createVehicle(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "make":"Toyota",
                      "model":"Corolla",
                      "category":"Sedan",
                      "price":22000,
                      "quantity":5
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.make").value("Toyota"));
    }




    @Test
    @WithMockUser
    void getAllVehicles_returns200() throws Exception {

        when(vehicleService.listVehicles())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    void getAllVehicles_returnsEmptyList() throws Exception {

        when(vehicleService.listVehicles())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
    @Test
    @WithMockUser
    void getAllVehicles_returnsVehicleList() throws Exception {

        VehicleResponse vehicle =
                new VehicleResponse(
                        UUID.randomUUID(),
                        "Toyota",
                        "Corolla",
                        "Sedan",
                        new BigDecimal("22000"),
                        5,
                        "ACTIVE"
                );

        when(vehicleService.listVehicles())
                .thenReturn(List.of(vehicle));

        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].make")
                        .value("Toyota"))
                .andExpect(jsonPath("$[0].model")
                        .value("Corolla"));
    }
    @Test
    @WithMockUser(roles = "USER")
    void getAllVehicles_returns200_forUser()
            throws Exception {

        when(vehicleService.listVehicles())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateVehicle_returns200_whenSuccessful() throws Exception {

        UUID id = UUID.randomUUID();

        VehicleResponse response = new VehicleResponse(
                id,
                "Honda",
                "City",
                "Sedan",
                new BigDecimal("25000"),
                10,
                "ACTIVE"
        );

        when(vehicleService.updateVehicle(eq(id), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/vehicles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "make":"Honda",
                      "model":"City",
                      "category":"Sedan",
                      "price":25000,
                      "quantity":10
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Honda"))
                .andExpect(jsonPath("$.model").value("City"));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateVehicle_returns400_whenPriceNegative() throws Exception {

        UUID id = UUID.randomUUID();

        mockMvc.perform(put("/api/vehicles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "make":"Honda",
                      "model":"City",
                      "category":"Sedan",
                      "price":-100,
                      "quantity":10
                    }
                    """))
                .andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(roles = "USER")
    void updateVehicle_returns200_forAuthenticatedUser() throws Exception {

        UUID id = UUID.randomUUID();

        VehicleResponse response =
                new VehicleResponse(
                        id,
                        "Honda",
                        "City",
                        "Sedan",
                        new BigDecimal("25000"),
                        10,
                        "ACTIVE"
                );

        when(vehicleService.updateVehicle(eq(id), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/vehicles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "make":"Honda",
                      "model":"City",
                      "category":"Sedan",
                      "price":25000,
                      "quantity":10
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Honda"));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteVehicle_returns204_whenSuccessful() throws Exception {

        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/vehicles/{id}", id))
                .andExpect(status().isNoContent());

        verify(vehicleService).deleteVehicle(id);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteVehicle_returns404_whenVehicleNotFound() throws Exception {

        UUID id = UUID.randomUUID();

        doThrow(new ResourceNotFoundException(
                "Vehicle not found"))
                .when(vehicleService)
                .deleteVehicle(id);

        mockMvc.perform(delete("/api/vehicles/{id}", id))
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(roles = "USER")
    void deleteVehicle_returns403_forUser() throws Exception {

        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/vehicles/{id}", id))
                .andExpect(status().isForbidden());

        verify(vehicleService, never())
                .deleteVehicle(any());
    }
    @Test
    @WithMockUser
    void searchVehicles_filtersByMake() throws Exception {

        VehicleResponse vehicle =
                new VehicleResponse(
                        UUID.randomUUID(),
                        "Toyota",
                        "Corolla",
                        "Sedan",
                        new BigDecimal("22000"),
                        5,
                        "ACTIVE"
                );

        when(vehicleService.searchVehicles(any()))
                .thenReturn(new PageImpl<>(List.of(vehicle)));

        mockMvc.perform(
                        get("/api/vehicles/search")
                                .param("make", "Toyota")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].make")
                        .value("Toyota"));
    }
    @Test
    @WithMockUser
    void searchVehicles_returnsEmptyPage() throws Exception {

        when(vehicleService.searchVehicles(any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/vehicles/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }
    @Test
    @WithMockUser
    void searchVehicles_supportsPagination() throws Exception {

        when(vehicleService.searchVehicles(any()))
                .thenReturn(Page.empty());

        mockMvc.perform(
                        get("/api/vehicles/search")
                                .param("page", "1")
                                .param("size", "5")
                )
                .andExpect(status().isOk());
    }
}