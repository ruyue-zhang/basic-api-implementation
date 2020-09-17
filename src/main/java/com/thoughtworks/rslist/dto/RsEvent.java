package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
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
