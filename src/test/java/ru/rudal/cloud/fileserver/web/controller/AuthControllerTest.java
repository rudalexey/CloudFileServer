package ru.rudal.cloud.fileserver.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.rudal.cloud.fileserver.DefaultTestAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Rud
 */
@AutoConfigureMockMvc
@DefaultTestAnnotations
class AuthControllerTest {
    @LocalServerPort
    int port;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUnauthorizedGetRequestByLoginURL() throws Exception {
        mockMvc.perform(get("/api/auth/login")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }
    @Test
    public void testUnauthorized() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/actuator/health")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/actuator/info")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/actuator")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/swagger-resources")).andExpect(status().isUnauthorized());

    }
    @Test
    void authenticateUser() throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(
                "{\"password\": \"password\",\"username\": \"admin\"}"
        ))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Map map = objectMapper.readValue(json, Map.class);
        String token = (String) map.get("token");
        assertNotNull(token);
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
    }
}
