package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name")
    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String phone;
    private Integer vote;
}
