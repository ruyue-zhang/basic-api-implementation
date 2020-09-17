package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.exception.InvalidParamException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private final List<RsEvent> rsList = initRsList();

  private List<RsEvent> initRsList() {
    User user = new User("zoom", "female", 18, "123@twuc.com", "18888888888");
    List<RsEvent> tempRsList = new ArrayList<>();
    tempRsList.add(new RsEvent("第一条事件", "无分类", user));
    tempRsList.add(new RsEvent("第二条事件", "无分类", user));
    tempRsList.add(new RsEvent("第三条事件", "无分类", user));
    return tempRsList;
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
    Boolean isExist = false;
    for (User user : UserController.userList) {
      if (user.getName().equals(rsEvent.getUser().getName())) {
        isExist = true;
        break;
      }
    }
    if(!isExist) {
      UserController.userList.add(rsEvent.getUser());
    }
    rsList.add(rsEvent);
    return ResponseEntity.created(null)
            .header("index", String.valueOf(rsList.size() - 1))
            .build();
  }

  @DeleteMapping("/rs/delete/{index}")
  public ResponseEntity deleteRsEvent(@PathVariable int index) {
    rsList.remove(index - 1);
    return ResponseEntity.created(null).build();
  }

  @PutMapping("/rs/put/{index}")
  public ResponseEntity updateRsByIndex(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    String eventName = rsEvent.getEventName() == null ? rsList.get(index - 1).getEventName() : rsEvent.getEventName();
    String keyWord = rsEvent.getKeyWord() == null ? rsList.get(index- 1).getKeyWord() : rsEvent.getKeyWord();
    rsList.set(index - 1, new RsEvent(eventName, keyWord, rsEvent.getUser()));
    return ResponseEntity.created(null).build();
  }
}
