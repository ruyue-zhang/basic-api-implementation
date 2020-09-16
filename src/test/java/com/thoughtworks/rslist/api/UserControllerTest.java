package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_register_user() throws Exception {
        User user = new User("zoom", "female", 18, "123@twuc.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}