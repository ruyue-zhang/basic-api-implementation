package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.exception.InvalidParamException;
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

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody User user, BindingResult bindingResult) throws InvalidParamException {
        if(bindingResult.hasErrors()) {
            throw new InvalidParamException("invalid user");
        }
        userList.add(user);
        return ResponseEntity.created(null)
                .header("index", String.valueOf(userList.size() - 1))
                .build();
    }

    @GetMapping("/get/users")
    public ResponseEntity getAllUser() {
        return ResponseEntity.ok()
                .body(userList);
    }
}
