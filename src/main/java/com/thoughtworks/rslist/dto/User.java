package com.thoughtworks.rslist.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class User {
    @NotEmpty
    @Size(max = 8)
    private String name;
    @NotEmpty
    private String gender;
    @NotNull
    private Integer age;
    private String email;
    private String phone;
    private Integer vote = 10;

    public User(String name, String gender, Integer age, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }
}
