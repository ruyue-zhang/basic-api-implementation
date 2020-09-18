package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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
    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }


    @Test
    void should_register_user() throws Exception {
        User user = new User("zoom", "female", 18, "123@twuc.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register")
                .content(userStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", "1"))
                .andExpect(status().isCreated());

        List<UserEntity> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals("zoom", users.get(0).getName());
        assertEquals("female", users.get(0).getGender());
        assertEquals(18, users.get(0).getAge());
        assertEquals("123@twuc.com", users.get(0).getEmail());
        assertEquals("18888888888", users.get(0).getPhone());
        assertEquals(10, users.get(0).getVote());
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
                .andExpect(header().string("index", "1"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/get/users"))
                .andExpect(status().isOk());

        assertEquals(1, userRepository.findAll().size());
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
                .andExpect(jsonPath("$.name", is("zhangSan")))
                .andExpect(jsonPath("$.gender", is("female")))
                .andExpect(jsonPath("$.age", is(25)))
                .andExpect(jsonPath("$.email", is("666@twuc.com")))
                .andExpect(jsonPath("$.phone", is("18800000000")))
                .andExpect(jsonPath("$.vote", is(10)));
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
                .andExpect(status().isNoContent());

        List<UserEntity> userList = userRepository.findAll();
        assertEquals(0,userList.size());
    }

    @Test
    void should_delete_rsEvent_When_delete_user() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("zhangSan")
                .gender("female")
                .age(25)
                .email("666@twuc.com")
                .phone("18800000000")
                .vote(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("多喝水的好处")
                .keyWord("健康")
                .userId(userEntity.getId())
                .build();
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(delete("/user/{id}", userEntity.getId()))
                .andExpect(status().isNoContent());
        List<UserEntity> userList = userRepository.findAll();
        List<RsEventEntity> rsEventList = rsEventRepository.findAll();
        assertEquals(0,userList.size());
        assertEquals(0,rsEventList.size());
    }
}