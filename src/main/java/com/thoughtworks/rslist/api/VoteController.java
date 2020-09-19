package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    final VoteRepository voteRepository;

    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/votes")
    public List<Vote> getVotes(@RequestParam(required = false) String start,
                               @RequestParam(required = false) String end) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start,df);
        LocalDateTime endTime = LocalDateTime.parse(start,df);

        return voteRepository.findByVoteTimeBetween(startTime, endTime).stream().map(voteEntity -> Vote.builder()
                    .voteNum(voteEntity.getVoteNum())
                .voteTime(voteEntity.getVoteTime())
                .userId(voteEntity.getUserEntity().getId())
                .rsEventId(voteEntity.getRsEventEntity().getId())
                .build()
        ).collect(Collectors.toList());
    }
}
