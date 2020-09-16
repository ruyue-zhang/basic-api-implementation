package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    public interface WithoutUserView {};

    @NotEmpty
    private String eventName;
    @NotEmpty
    private String keyWord;
    @NotNull
    @Valid
    @JsonIgnore
    private User user;

    @JsonView(WithoutUserView.class)
    public User getUser() {
        return user;
    }
}
