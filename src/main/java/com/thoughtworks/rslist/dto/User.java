package com.thoughtworks.rslist.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class User {
    @NotEmpty
    @Size(max = 8)
    private String name;
    @NotEmpty
    private String gender;
    @NotNull
    @Min(18)
    @Max(100)
    private Integer age;
    @Email
    private String email;
    @Pattern(regexp = "^1\\d{10}$")
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
