package com.archi.car_dealership_backend.inventory.controller;

import com.archi.car_dealership_backend.auth.util.JwtUtil;
import com.archi.car_dealership_backend.config.SecurityConfig;
import com.archi.car_dealership_backend.inventory.service.InventoryService;
import com.archi.car_dealership_backend.security.CustomUserDetailsService;
import com.archi.car_dealership_backend.security.JwtAccessDeniedHandler;
import com.archi.car_dealership_backend.security.JwtAuthenticationEntryPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(SecurityConfig.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockitoBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Test
    @WithMockUser(username = "archi@test.com")
    void purchaseVehicle_returns200() throws Exception {

        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/api/vehicles/{id}/purchase", id)
                        .param("quantity", "2"))
                .andExpect(status().isOk());

        verify(inventoryService)
                .purchaseVehicle(eq(id), eq(2), eq("archi@test.com"));
    }

//    @Test
//    void purchaseVehicle_returns401_whenUnauthenticated() throws Exception {
//
//        UUID id = UUID.randomUUID();
//
//        mockMvc.perform(post("/api/vehicles/{id}/purchase", id)
//                        .param("quantity", "2"))
//                .andExpect(status().isUnauthorized());
//    }

    @Test
    @WithMockUser
    void purchaseVehicle_returns400_whenQuantityInvalid() throws Exception {

        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/api/vehicles/{id}/purchase", id)
                        .param("quantity", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "archi@test.com")
    void purchaseVehicle_callsServiceOnce() throws Exception {

        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/api/vehicles/{id}/purchase", id)
                        .param("quantity", "1"))
                .andExpect(status().isOk());

        verify(inventoryService, times(1))
                .purchaseVehicle(eq(id), eq(1), eq("archi@test.com"));
    }
    @Test
    @WithMockUser(username = "admin@test.com")
    void restockVehicle_returns200() throws Exception {

        UUID id = UUID.randomUUID();

        mockMvc.perform(
                        post("/api/vehicles/{id}/restock", id)
                                .param("quantity", "5")
                )
                .andExpect(status().isOk());

        verify(inventoryService)
                .restockVehicle(
                        eq(id),
                        eq(5),
                        eq("admin@test.com")
                );
    }
}