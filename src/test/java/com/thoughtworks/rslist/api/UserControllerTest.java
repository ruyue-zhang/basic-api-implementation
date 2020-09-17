package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.hamcrest.Matchers.hasKey;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }


    @Test
    void should_register_user() throws Exception {
        User user = new User("zoom", "female", 18, "123@twuc.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", String.valueOf("1")))
                .andExpect(status().isCreated());

        List<UserEntity> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals("zoom", users.get(0).getName());
    }

    @Test
    void should_not_register_when_name_is_empty() throws Exception {
        User user = new User("", "female", 18, "123@twuc.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_when_name_is_null() throws Exception {
        User user = new User(null, "female", 18, "123@twuc.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_when_name_length_more_than_8() throws Exception {
        User user = new User("administration", "female", 18, "123@twuc.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_when_gender_is_empty() throws Exception {
        User user = new User("zoom", "", 18, "123@twuc.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_when_gender_is_null() throws Exception {
        User user = new User("zoom", null, 18, "123@twuc.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_when_age_is_null() throws Exception {
        User user = new User("zoom", "female", null, "123@twuc.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_when_age_less_than_18() throws Exception {
        User user = new User("zoom", "female", 16, "123@twuc.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_when_age_more_than_100() throws Exception {
        User user = new User("zoom", "female", 101, "123@twuc.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_when_email_is_invalid() throws Exception {
        User user = new User("zoom", "female", 18, "@twuc.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_when_phone_not_start_1() throws Exception {
        User user = new User("zoom", "female", 18, "123@twuc.com", "28888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_when_phone_length_is_not_11() throws Exception {
        User user = new User("zoom", "female", 18, "123@twuc.com", "12345678");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_when_phone_is_null() throws Exception {
        User user = new User("zoom", "female", 18, "123@twuc.com", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_get_all_users() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", String.valueOf(UserController.userList.size() - 1)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/get/users"))
                .andExpect(jsonPath("$[0].name", is("xiaowang")))
                .andExpect(jsonPath("$[0].gender", is("female")))
                .andExpect(jsonPath("$[0].age", is(19)))
                .andExpect(jsonPath("$[0].email", is("a@thoughtworks.com")))
                .andExpect(jsonPath("$[0].phone", is("18888888888")))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_exception_when_add_user_invalid() throws Exception {
        User user = new User("zhangSan7777", "male", 25, "666@twuc.com", "18800000000");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("invalid user")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void return_user_info_when_find_by_id() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("zhangSan")
                .gender("female")
                .age(25)
                .email("666@twuc.com")
                .phone("18800000000")
                .vote(10)
                .build();
        userRepository.save(userEntity);

        mockMvc.perform(get("/user/{id}", userEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("zhangSan")));
    }

    @Test
    void should_delete_user_info_from_db_by_id() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("zhangSan")
                .gender("female")
                .age(25)
                .email("666@twuc.com")
                .phone("18800000000")
                .vote(10)
                .build();
        userRepository.save(userEntity);

        mockMvc.perform(delete("/user/{id}", userEntity.getId()))
                .andExpect(status().isCreated());
        List<UserEntity> userList = userRepository.findAll();
        assertEquals(0,userList.size());
    }
}