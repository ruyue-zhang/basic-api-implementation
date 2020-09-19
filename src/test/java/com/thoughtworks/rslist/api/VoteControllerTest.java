package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    @Test
    void should_return_vote_record_between_start_and_end_time() throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = df.format(LocalDateTime.now());

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
        VoteEntity voteEntity = VoteEntity.builder()
                .userEntity(userEntity)
                .rsEventEntity(rsEventEntity)
                .voteNum(1)
                .voteTime(LocalDateTime.now())
                .build();
        voteRepository.save(voteEntity);

        String end = df.format(LocalDateTime.now());
        mockMvc.perform(get("/votes?start={start}&end={end}",start, end))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId", is(userEntity.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)));
    }
}