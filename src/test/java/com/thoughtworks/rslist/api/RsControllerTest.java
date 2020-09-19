package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime voteTime;
    @BeforeEach
    void setUp() {
        voteTime = LocalDateTime.now();
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    @Test
    void should_get_rs_list() throws Exception {
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
                .eventName("猪肉涨价了")
                .keyWord("经济")
                .userId(userEntity.getId())
                .build();
        rsEventRepository.save(rsEventEntity);
        rsEventEntity = RsEventEntity.builder()
                .eventName("苹果发布会")
                .keyWord("科技")
                .userId(userEntity.getId())
                .build();
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyWord", is("经济")))
                .andExpect(jsonPath("$[0]", not(hasKey("userId"))))
                .andExpect(jsonPath("$[1].eventName", is("苹果发布会")))
                .andExpect(jsonPath("$[1].keyWord", is("科技")))
                .andExpect(jsonPath("$[1]", not(hasKey("userId"))));
    }

    @Test
    void should_get_one_rs_event() throws Exception {
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
                .eventName("猪肉涨价了")
                .keyWord("经济")
                .userId(userEntity.getId())
                .build();
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/event/{id}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$.keyWord", is("经济")))
                .andExpect(jsonPath("$", not(hasKey("userId"))));
    }

    @Test
    void should_get_rs_event_by_range() throws Exception {
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
                .eventName("猪肉涨价了")
                .keyWord("经济")
                .userId(userEntity.getId())
                .build();
        rsEventRepository.save(rsEventEntity);

        rsEventEntity = RsEventEntity.builder()
                .eventName("苹果发布会")
                .keyWord("科技")
                .userId(userEntity.getId())
                .build();
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/events?start=1&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyWord", is("经济")))
                .andExpect(jsonPath("$[0]", not(hasKey("userId"))))
                .andExpect(jsonPath("$[1].eventName", is("苹果发布会")))
                .andExpect(jsonPath("$[1].keyWord", is("科技")))
                .andExpect(jsonPath("$[1]", not(hasKey("userId"))));
    }

    @Test
    void should_delete_rs_event_by_index() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("zhangSan")
                .gender("female")
                .age(25)
                .email("666@twuc.com")
                .phone("18800000000")
                .vote(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity1 = RsEventEntity.builder()
                .eventName("猪肉涨价了")
                .keyWord("经济")
                .userId(userEntity.getId())
                .build();
        rsEventRepository.save(rsEventEntity1);

        RsEventEntity rsEventEntity2 = RsEventEntity.builder()
                .eventName("苹果发布会")
                .keyWord("科技")
                .userId(userEntity.getId())
                .build();
        rsEventRepository.save(rsEventEntity2);

        mockMvc.perform(delete("/rs/event/{id}", rsEventEntity1.getId()))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventName", is("苹果发布会")))
                .andExpect(jsonPath("$[0].keyWord", is("科技")));
    }


    @Test
    void should_not_add_when_eventName_is_empty() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("zhangSan")
                .gender("female")
                .age(25)
                .email("666@twuc.com")
                .phone("18800000000")
                .vote(10)
                .build();
        userRepository.save(userEntity);

        RsEvent rsEvent = new RsEvent("", "娱乐", userEntity.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String rsString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(rsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_when_keyWord_is_null() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("zhangSan")
                .gender("female")
                .age(25)
                .email("666@twuc.com")
                .phone("18800000000")
                .vote(10)
                .build();
        userRepository.save(userEntity);

        RsEvent rsEvent = new RsEvent("林俊杰发新歌了！", null, userEntity.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String rsString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(rsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_when_keyWord_is_empty() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("zhangSan")
                .gender("female")
                .age(25)
                .email("666@twuc.com")
                .phone("18800000000")
                .vote(10)
                .build();
        userRepository.save(userEntity);

        RsEvent rsEvent = new RsEvent("林俊杰发新歌了！", "", userEntity.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String rsString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(rsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_exception_when_input_invalid_index() throws Exception {
        mockMvc.perform(get("/rs/event/10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    void should_return_exception_when_get_with_invalid_param() throws Exception {
        mockMvc.perform(get("/rs/events?start=1&end=20"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_return_exception_when_add_rs_event_invalid() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserEntity userEntity = UserEntity.builder()
                .name("zhangSan")
                .gender("female")
                .age(25)
                .email("666@twuc.com")
                .phone("18800000000")
                .vote(10)
                .build();
        userRepository.save(userEntity);
        RsEvent rsEvent = new RsEvent(null, "娱乐", userEntity.getId());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        objectMapper.writerWithView(RsEvent.WithUserView.class).writeValue(bos, rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(bos.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("invalid param")))
                .andExpect(status().isBadRequest());
    }

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

    @Test
    void should_update_rs_event_when_user_id_match() throws Exception {
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
                .eventName("猪肉涨价了")
                .keyWord("经济")
                .userId(userEntity.getId())
                .build();
        rsEventRepository.save(rsEventEntity);

        RsEvent newRsEvent;
        ObjectMapper objectMapper = new ObjectMapper();

        newRsEvent = new RsEvent("第一条event", null, userEntity.getId());
        String changeEventName = objectMapper.writeValueAsString(newRsEvent);
        mockMvc.perform(put("/rs/event/{id}", rsEventEntity.getId())
                .content(changeEventName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        RsEventEntity resultRsEvent = rsEventRepository.findById(rsEventEntity.getId()).get();
        assertEquals("第一条event", resultRsEvent.getEventName());
        assertEquals("经济", resultRsEvent.getKeyWord());

        newRsEvent = new RsEvent(null, "noClassify", userEntity.getId());
        String changeKeyWord = objectMapper.writeValueAsString(newRsEvent);
        mockMvc.perform(put("/rs/event/{id}", rsEventEntity.getId())
                .content(changeKeyWord)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        resultRsEvent = rsEventRepository.findById(rsEventEntity.getId()).get();
        assertEquals("第一条event", resultRsEvent.getEventName());
        assertEquals("noClassify", resultRsEvent.getKeyWord());


        newRsEvent = new RsEvent("第三条event", "zoom", userEntity.getId());
        String changeBoth = objectMapper.writeValueAsString(newRsEvent);
        mockMvc.perform(put("/rs/event/{id}", rsEventEntity.getId())
                .content(changeBoth)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        resultRsEvent = rsEventRepository.findById(rsEventEntity.getId()).get();
        assertEquals("第三条event", resultRsEvent.getEventName());
        assertEquals("zoom", resultRsEvent.getKeyWord());
    }

    @Test
    void should_not_update_rs_event_when_user_id_not_match() throws Exception {
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
                .eventName("猪肉涨价了")
                .keyWord("经济")
                .userId(userEntity.getId())
                .build();
        rsEventRepository.save(rsEventEntity);

        RsEvent newRsEvent;
        ObjectMapper objectMapper = new ObjectMapper();

        newRsEvent = new RsEvent("第一条event", "测试", 3);
        String changeEventName = objectMapper.writeValueAsString(newRsEvent);
        mockMvc.perform(put("/rs/event/{id}", rsEventEntity.getId())
                .content(changeEventName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_vote_rs_event_when_user_vote_num_more_than_curr_vote_num() throws Exception {
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
                .eventName("猪肉涨价了")
                .keyWord("经济")
                .userId(userEntity.getId())
                .build();
        rsEventRepository.save(rsEventEntity);

        Vote vote = Vote.builder()
                .userId(userEntity.getId())
                .voteNum(5)
                .voteTime(voteTime)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String voteJson = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/rs/{id}/vote", rsEventEntity.getId())
                .content(voteJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(5, userRepository.findById(userEntity.getId()).get().getVote());
    }

    @Test
    void should_return_bad_request_when_user_vote_num_less_than_curr_vote_num() throws Exception {
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
                .eventName("猪肉涨价了")
                .keyWord("经济")
                .userId(userEntity.getId())
                .build();
        rsEventRepository.save(rsEventEntity);

        Vote vote = Vote.builder()
                .userId(userEntity.getId())
                .voteNum(11)
                .voteTime(voteTime)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String voteJson = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/rs/{id}/vote", rsEventEntity.getId())
                .content(voteJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertEquals(10, userRepository.findById(userEntity.getId()).get().getVote());
    }
}