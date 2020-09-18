package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    public interface WithoutUserView {};
    public interface WithUserView extends WithoutUserView{};

    @NotEmpty
    private String eventName;
    @NotEmpty
    private String keyWord;
    @NotNull
    private int userId;
    private int voteNum = 0;

    public RsEvent(@NotEmpty String eventName, @NotEmpty String keyWord, @NotNull int userId) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userId = userId;
    }

    @JsonView(WithUserView.class)
    public int getUserId() {
        return userId;
    }

    @JsonView(WithoutUserView.class)
    public String getEventName() {
        return eventName;
    }

    @JsonView(WithoutUserView.class)
    public String getKeyWord() {
        return keyWord;
    }
}
