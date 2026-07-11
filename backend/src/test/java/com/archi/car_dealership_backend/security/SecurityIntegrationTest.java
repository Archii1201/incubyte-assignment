package com.archi.car_dealership_backend.security;

import com.archi.car_dealership_backend.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createVehicle_returns401_withoutToken() throws Exception {

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
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createVehicle_returns401_withInvalidToken() throws Exception {

        mockMvc.perform(post("/api/vehicles")
                        .header("Authorization", "Bearer invalid-token")
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
                .andExpect(status().isUnauthorized());
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
    @Test
    void getAllVehicles_returns401_withoutAuthentication()
            throws Exception {

        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isUnauthorized());
    }

}