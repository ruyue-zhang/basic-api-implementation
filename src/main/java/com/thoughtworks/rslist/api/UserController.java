package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    public static List<User>  userList = new ArrayList<>();
    final UserRepository userRepository;
    final RsEventRepository rsEventRepository;

    public UserController(UserRepository userRepository, RsEventRepository rsEventRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
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
                .header("index", String.valueOf(userRepository.findAll().size()))
                .build();
    }

    @GetMapping("/get/users")
    public ResponseEntity getAllUser() {
        return ResponseEntity.ok()
                .body(userList);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) throws InvalidIndexException {
        Optional<UserEntity> result = userRepository.findById(id);
        if(!result.isPresent()) {
            throw new InvalidIndexException("invalid user id");
        }
        UserEntity userEntity = result.get();
        return ResponseEntity.ok(User.builder()
                .name(userEntity.getName())
                .gender(userEntity.getGender())
                .age(userEntity.getAge())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .vote(userEntity.getVote())
                .build());
    }

    @Transactional
    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUserById(@PathVariable Integer id) throws InvalidIndexException {
        Optional<UserEntity> result = userRepository.findById(id);
        if(!result.isPresent()) {
            throw new InvalidIndexException("invalid user id");
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
