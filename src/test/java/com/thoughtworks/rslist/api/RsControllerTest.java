package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_get_rs_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
    }

    @Test
    void should_get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无分类")));

        mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("无分类")));

        mockMvc.perform(get("/rs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("无分类")));
    }

    @Test
    void should_get_rs_event_by_range() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
    }

    @Test
    void should_add_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(rsString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyWord", is("经济")));
    }

    @Test
    void should_delete_rs_event_by_index() throws Exception {
        mockMvc.perform(delete("/rs/delete/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")));
    }

    @Test
    void should_update_rs_List_by_index() throws Exception {
        RsEvent rsEvent;
        ObjectMapper objectMapper = new ObjectMapper();

        rsEvent = new RsEvent("第一条event", null, null);
        String changeEventName = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/put/1")
                .content(changeEventName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第一条event")))
                .andExpect(jsonPath("$.keyWord", is("无分类")));


        rsEvent = new RsEvent(null, "noClassify", null);
        String changeKeyWord = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/put/2")
                .content(changeKeyWord)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("noClassify")));


        rsEvent = new RsEvent("第三条event", "noClassify", null);
        String changeBoth = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/put/3")
                .content(changeBoth)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第三条event")))
                .andExpect(jsonPath("$.keyWord", is("noClassify")));
    }

    @Test
    void should_not_add_when_eventName_is_null() throws Exception {
        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
        RsEvent rsEvent = new RsEvent(null, "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(rsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_when_eventName_is_empty() throws Exception {
        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
        RsEvent rsEvent = new RsEvent("", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(rsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_when_keyWord_is_null() throws Exception {
        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
        RsEvent rsEvent = new RsEvent("林俊杰发新歌了！", null, user);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(rsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_when_keyWord_is_empty() throws Exception {
        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
        RsEvent rsEvent = new RsEvent("林俊杰发新歌了！", "", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(rsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_when_user_is_null() throws Exception {
        RsEvent rsEvent = new RsEvent("林俊杰发新歌了！", "娱乐", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(rsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_user_when_user_not_exist() throws Exception {
        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
        RsEvent rsEvent = new RsEvent("林俊杰发新歌了!", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(rsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, UserController.userList.size());
    }

    @Test
    void should_not_add_user_when_user_exist() throws Exception {
        User user = new User("zhangSan", "male", 25, "666@twuc.com", "18800000000");
        ObjectMapper objectMapper = new ObjectMapper();

        RsEvent rsEvent = new RsEvent("林俊杰发新歌了!", "娱乐", user);
        String rsString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(rsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        RsEvent rsEventNew = new RsEvent("研究说明晚睡会秃头！!", "生活", user);
        String rsStringNew = objectMapper.writeValueAsString(rsEventNew);

        mockMvc.perform(post("/rs/event")
                .content(rsStringNew)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, UserController.userList.size());
    }
}