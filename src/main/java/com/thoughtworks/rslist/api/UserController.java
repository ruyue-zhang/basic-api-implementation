package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public static List<User>  userList = new ArrayList<>();
    final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody User user, BindingResult bindingResult) throws InvalidParamException {
        if(bindingResult.hasErrors()) {
            throw new InvalidParamException("invalid user");
        }
        UserEntity userEntity = UserEntity.builder()
                .name(user.getName())
                .gender(user.getGender())
                .age(user.getAge())
                .email(user.getEmail())
                .phone(user.getPhone())
                .vote(user.getVote())
                .build();

        userRepository.save(userEntity);
        return ResponseEntity.created(null)
                .header("index", String.valueOf(userEntity.getId()))
                .build();
    }

    @GetMapping("/get/users")
    public ResponseEntity getAllUser() {
        return ResponseEntity.ok()
                .body(userList);
    }
}
