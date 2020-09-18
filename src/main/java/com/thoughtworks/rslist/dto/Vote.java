package com.thoughtworks.rslist.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    private int userId;
    private int rsEventId = 0;
    private LocalDateTime voteTime;
    private int voteNum;

    public Vote(int userId, LocalDateTime voteTime, int voteNum) {
        this.userId = userId;
        this.voteTime = voteTime;
        this.voteNum = voteNum;
    }
}
