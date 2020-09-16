package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
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

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventByRange(@RequestParam(required = false) Integer start,
                                         @RequestParam(required = false) Integer end) {
    if(start == null || end == null) {
      return rsList;
    }
    return rsList.subList(start - 1, end);
  }

  @GetMapping("/rs/{index}")
  public RsEvent getRsEvent(@PathVariable int index) {
    return rsList.get(index -1);
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@Valid  @RequestBody RsEvent rsEvent) {
    rsList.add(rsEvent);
  }

  @DeleteMapping("/rs/delete/{index}")
  public void deleteRsEvent(@PathVariable int index) {
    rsList.remove(index - 1);
  }

  @PutMapping("/rs/put/{index}")
  public void updateRsByIndex(@PathVariable int index, @RequestBody String rsEventStr) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent rsEvent = objectMapper.readValue(rsEventStr, RsEvent.class);
    String eventName = rsEvent.getEventName() == null ? rsList.get(index - 1).getEventName() : rsEvent.getEventName();
    String keyWord = rsEvent.getKeyWord() == null ? rsList.get(index- 1).getKeyWord() : rsEvent.getKeyWord();
    rsList.set(index - 1, new RsEvent(eventName, keyWord, rsEvent.getUser()));
  }
}
