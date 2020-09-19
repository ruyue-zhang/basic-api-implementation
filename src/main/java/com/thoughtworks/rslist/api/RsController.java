package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class RsController {
  final UserRepository userRepository;
  final RsEventRepository rsEventRepository;
  final RsEventService rsEventService;
  final VoteRepository voteRepository;

  public RsController(RsEventRepository rsEventRepository, UserRepository userRepository, RsEventService rsEventService, VoteRepository voteRepository) {
    this.rsEventRepository = rsEventRepository;
    this.userRepository = userRepository;
    this.rsEventService = rsEventService;
    this.voteRepository = voteRepository;
  }

  private List<RsEvent> initRsList() {
    return new ArrayList<>();
  }

  @JsonView(RsEvent.WithoutUserView.class)
  @ResponseStatus(code = HttpStatus.OK)
  @GetMapping("/rs/events")
  public List<RsEvent> getRsEventByRange(@RequestParam(required = false) Integer start,
                                                        @RequestParam(required = false) Integer end) throws InvalidIndexException {
    if(start == null || end == null) {
      return rsEventRepository.findAll().stream().map(rsEventEntity -> RsEvent.builder()
              .eventName(rsEventEntity.getEventName())
              .keyWord(rsEventEntity.getKeyWord())
              .userId(rsEventEntity.getUserId())
              .voteNum(rsEventEntity.getVoteNum())
              .build()
      ).collect(Collectors.toList());
    }
    if (start < 1 || end < start) {
      throw new InvalidIndexException("invalid request param");
    }
    Optional<RsEventEntity> endRsEvent = rsEventRepository.findById(end);
    if(!endRsEvent.isPresent()) {
      throw new InvalidIndexException("invalid request param");
    }
    return rsEventRepository.findByIdBetween(start, end).stream().map(rsEventEntity -> RsEvent.builder()
            .eventName(rsEventEntity.getEventName())
            .keyWord(rsEventEntity.getKeyWord())
            .userId(rsEventEntity.getUserId())
            .voteNum(rsEventEntity.getVoteNum())
            .build()
    ).collect(Collectors.toList());
  }

  @ResponseStatus(code = HttpStatus.OK)
  @JsonView(RsEvent.WithoutUserView.class)
  @GetMapping("/rs/event/{id}")
  public RsEvent getRsEvent(@PathVariable int id) throws InvalidIndexException {
    Optional<RsEventEntity> rsEventEntity = rsEventRepository.findById(id);
    if(!rsEventEntity.isPresent()) {
      throw new InvalidIndexException("invalid index");
    }
    return RsEvent.builder()
            .eventName(rsEventEntity.get().getEventName())
            .keyWord(rsEventEntity.get().getKeyWord())
            .userId(rsEventEntity.get().getUserId())
            .build();
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent, BindingResult bindingResult) throws InvalidParamException {
    if (bindingResult.hasErrors()) {
      throw new InvalidParamException("invalid param");
    }
    if(!userRepository.existsById(rsEvent.getUserId())) {
      return ResponseEntity.badRequest().build();
    }
    RsEventEntity rsEventEntity = RsEventEntity.builder()
            .eventName(rsEvent.getEventName())
            .keyWord(rsEvent.getKeyWord())
            .userId(rsEvent.getUserId())
            .build();

    rsEventRepository.save(rsEventEntity);
    return ResponseEntity.created(null)
            .build();
  }

  @DeleteMapping("/rs/event/{id}")
  public ResponseEntity deleteRsEvent(@PathVariable int id) {
    rsEventRepository.deleteById(id);
    return ResponseEntity.created(null).build();
  }

  @PutMapping("/rs/event/{id}")
  public ResponseEntity updateRsByIndex(@PathVariable int id, @RequestBody RsEvent rsEvent) {
    RsEventEntity rsEventEntity = rsEventRepository.findById(id).get();
    if(rsEventEntity.getUserId() != rsEvent.getUserId()) {
      return ResponseEntity.badRequest().build();
    }
    String eventName = rsEvent.getEventName() == null ? rsEventEntity.getEventName() : rsEvent.getEventName();
    String keyWord = rsEvent.getKeyWord() == null ? rsEventEntity.getKeyWord() : rsEvent.getKeyWord();
    int userId = rsEventEntity.getUserId();

    rsEventEntity.setEventName(eventName);
    rsEventEntity.setKeyWord(keyWord);
    rsEventEntity.setUserId(userId);
    rsEventRepository.save(rsEventEntity);
    return ResponseEntity.created(null).build();
  }

  @PostMapping("/rs/{id}/vote")
  public ResponseEntity vote(@PathVariable int id, @RequestBody Vote vote) throws InvalidParamException {
    rsEventService.vote(id, vote);
    return ResponseEntity.created(null)
            .build();
  }
}
