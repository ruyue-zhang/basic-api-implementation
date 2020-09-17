package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  final UserRepository userRepository;
  final RsEventRepository rsEventRepository;
  private final List<RsEvent> rsList = initRsList();

  public RsController(RsEventRepository rsEventRepository, UserRepository userRepository) {
    this.rsEventRepository = rsEventRepository;
    this.userRepository = userRepository;
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

//  @PutMapping("/rs/put/{index}")
//  public ResponseEntity updateRsByIndex(@PathVariable int index, @RequestBody RsEvent rsEvent) {
//    String eventName = rsEvent.getEventName() == null ? rsList.get(index - 1).getEventName() : rsEvent.getEventName();
//    String keyWord = rsEvent.getKeyWord() == null ? rsList.get(index- 1).getKeyWord() : rsEvent.getKeyWord();
//    rsList.set(index - 1, new RsEvent(eventName, keyWord, rsEvent.getUser()));
//    return ResponseEntity.created(null).build();
//  }
}
