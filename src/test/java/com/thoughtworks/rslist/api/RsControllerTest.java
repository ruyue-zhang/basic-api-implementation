package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsControllerTest {
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

//    @Test
//    void should_get_rs_list() throws Exception {
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2]", not(hasKey("user"))));
//    }
//
//    @Test
//    void should_get_one_rs_event() throws Exception {
//        mockMvc.perform(get("/rs/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第一条事件")))
//                .andExpect(jsonPath("$.keyWord", is("无分类")))
//                .andExpect(jsonPath("$", not(hasKey("user"))));
//
//        mockMvc.perform(get("/rs/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第二条事件")))
//                .andExpect(jsonPath("$.keyWord", is("无分类")))
//                .andExpect(jsonPath("$", not(hasKey("user"))));
//
//        mockMvc.perform(get("/rs/3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第三条事件")))
//                .andExpect(jsonPath("$.keyWord", is("无分类")))
//                .andExpect(jsonPath("$", not(hasKey("user"))));
//    }
//
//    @Test
//    void should_get_rs_event_by_range() throws Exception {
//        mockMvc.perform(get("/rs/list?start=1&end=3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2]", not(hasKey("user"))));
//    }
//
//    @Test
//    void should_add_one_rs_event() throws Exception {
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)));
//
//        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        objectMapper.writerWithView(RsEvent.WithUserView.class).writeValue(bos, rsEvent);
//
//        mockMvc.perform(post("/rs/event")
//                .content(bos.toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(header().string("index", "3"))
//                .andExpect(status().isCreated());
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(4)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
//                .andExpect(jsonPath("$[3].keyWord", is("经济")));
//    }
//
//    @Test
//    void should_delete_rs_event_by_index() throws Exception {
//        mockMvc.perform(delete("/rs/delete/1"))
//                .andExpect(status().isCreated());
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")));
//    }
//
//    @Test
//    void should_update_rs_List_by_index() throws Exception {
//        RsEvent rsEvent;
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        rsEvent = new RsEvent("第一条event", null, null);
//        String changeEventName = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(put("/rs/put/1")
//                .content(changeEventName)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//        mockMvc.perform(get("/rs/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第一条event")))
//                .andExpect(jsonPath("$.keyWord", is("无分类")));
//
//
//        rsEvent = new RsEvent(null, "noClassify", null);
//        String changeKeyWord = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(put("/rs/put/2")
//                .content(changeKeyWord)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//        mockMvc.perform(get("/rs/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第二条事件")))
//                .andExpect(jsonPath("$.keyWord", is("noClassify")));
//
//
//        rsEvent = new RsEvent("第三条event", "noClassify", null);
//        String changeBoth = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(put("/rs/put/3")
//                .content(changeBoth)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//        mockMvc.perform(get("/rs/3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第三条event")))
//                .andExpect(jsonPath("$.keyWord", is("noClassify")));
//    }
//
//    @Test
//    void should_not_add_when_eventName_is_null() throws Exception {
//        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
//        RsEvent rsEvent = new RsEvent(null, "娱乐", user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String rsString = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event")
//                .content(rsString)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_add_when_eventName_is_empty() throws Exception {
//        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
//        RsEvent rsEvent = new RsEvent("", "娱乐", user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String rsString = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event")
//                .content(rsString)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_add_when_keyWord_is_null() throws Exception {
//        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
//        RsEvent rsEvent = new RsEvent("林俊杰发新歌了！", null, user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String rsString = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event")
//                .content(rsString)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_add_when_keyWord_is_empty() throws Exception {
//        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
//        RsEvent rsEvent = new RsEvent("林俊杰发新歌了！", "", user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String rsString = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event")
//                .content(rsString)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_add_when_user_is_null() throws Exception {
//        RsEvent rsEvent = new RsEvent("林俊杰发新歌了！", "娱乐", null);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String rsString = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event")
//                .content(rsString)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_add_user_when_user_not_exist() throws Exception {
//        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
//        RsEvent rsEvent = new RsEvent("林俊杰发新歌了!", "娱乐", user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        objectMapper.writerWithView(RsEvent.WithUserView.class).writeValue(bos, rsEvent);
//
//        mockMvc.perform(post("/rs/event")
//                .content(bos.toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(header().string("index", "3"))
//                .andExpect(status().isCreated());
//
//        assertEquals(1, UserController.userList.size());
//    }
//
//    @Test
//    void should_not_add_user_when_user_exist() throws Exception {
//        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        RsEvent rsEvent = new RsEvent("林俊杰发新歌了!", "娱乐", user);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        objectMapper.writerWithView(RsEvent.WithUserView.class).writeValue(bos, rsEvent);
//
//        mockMvc.perform(post("/rs/event")
//                .content(bos.toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(header().string("index", "3"))
//                .andExpect(status().isCreated());
//
//        RsEvent rsEventNew = new RsEvent("研究说明晚睡会秃头！!", "生活", user);
//        objectMapper.writerWithView(RsEvent.WithUserView.class).writeValue(bos, rsEventNew);
//
//        mockMvc.perform(post("/rs/event")
//                .content(bos.toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(header().string("index", "4"))
//                .andExpect(status().isCreated());
//
//        assertEquals(1, UserController.userList.size());
//    }
//
//    @Test
//    void should_return_exception_when_input_invalid_index() throws Exception {
//        mockMvc.perform(get("/rs/10"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error", is("invalid index")));
//    }
//
//    @Test
//    void should_return_exception_when_get_with_invalid_param() throws Exception {
//        mockMvc.perform(get("/rs/list?start=1&end=20"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error", is("invalid request param")));
//    }
//
//    @Test
//    void should_return_exception_when_add_rs_event_invalid() throws Exception {
//        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        RsEvent rsEvent = new RsEvent(null, "娱乐", user);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        objectMapper.writerWithView(RsEvent.WithUserView.class).writeValue(bos, rsEvent);
//        mockMvc.perform(post("/rs/event")
//                .content(bos.toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.error", is("invalid param")))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    void should_add_rs_event_when_user_exist() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("zhangSan")
                .gender("female")
                .age(25)
                .email("666@twuc.com")
                .phone("18800000000")
                .vote(10)
                .build();
        userRepository.save(userEntity);

        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userEntity.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        objectMapper.writerWithView(RsEvent.WithUserView.class).writeValue(bos, rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(bos.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        assertEquals(1, rsEvents.size());
        assertEquals("猪肉涨价了", rsEvents.get(0).getEventName());
        assertEquals("经济", rsEvents.get(0).getKeyWord());
        assertEquals(userEntity.getId(), rsEvents.get(0).getUserId());
    }

    @Test
    void should_not_add_rs_event_when_user_not_exist() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", 1);
        ObjectMapper objectMapper = new ObjectMapper();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        objectMapper.writerWithView(RsEvent.WithUserView.class).writeValue(bos, rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(bos.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        assertEquals(0, rsEvents.size());
    }
}