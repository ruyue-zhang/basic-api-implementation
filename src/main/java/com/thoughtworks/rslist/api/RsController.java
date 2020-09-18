package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {
  final UserRepository userRepository;
  final RsEventRepository rsEventRepository;
  final RsEventService rsEventService;
  final VoteRepository voteRepository;
  private final List<RsEvent> rsList = initRsList();

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
  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEvent>> getRsEventByRange(@RequestParam(required = false) Integer start,
                                                        @RequestParam(required = false) Integer end) throws InvalidIndexException {
    if(start == null || end == null) {
      return ResponseEntity.ok().body(rsList);
    }
    if (start < 1 || end > rsList.size() || end < start) {
      throw new InvalidIndexException("invalid request param");
    }
    return ResponseEntity.ok().body(rsList.subList(start - 1, end));
  }

  @JsonView(RsEvent.WithoutUserView.class)
  @GetMapping("/rs/{index}")
  public ResponseEntity<RsEvent> getRsEvent(@PathVariable int index) throws InvalidIndexException {
    if(index > rsList.size()) {
      throw new InvalidIndexException("invalid index");
    }
    return ResponseEntity.ok().body(rsList.get(index -1));
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

  @DeleteMapping("/rs/delete/{index}")
  public ResponseEntity deleteRsEvent(@PathVariable int index) {
    rsList.remove(index - 1);
    return ResponseEntity.created(null).build();
  }

  @PutMapping("/rs/{rsEventId}")
  public ResponseEntity updateRsByIndex(@PathVariable int rsEventId, @RequestBody RsEvent rsEvent) {
    RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventId).get();
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

  @PostMapping("/rs/vote/{rsEventId}")
  public ResponseEntity vote(@PathVariable int rsEventId, @RequestBody Vote vote) throws InvalidParamException {
    rsEventService.vote(rsEventId, vote);
    return ResponseEntity.created(null)
            .build();
  }
}
