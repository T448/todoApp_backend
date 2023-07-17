package com.example.spring_project.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.model.User;
import com.example.spring_project.domain.service.UserService;

import de.huxhorn.sulky.ulid.ULID;

@Service
public class UserUsecase {
    @Autowired
    UserService userService;

    public User register(String email, String name) {
        User userInfo = userService.selectOneUser(email);
        if (userInfo == null) {
            ULID user_ulid = new ULID();
            String user_id = user_ulid.nextULID();
            User newUser = new User();
            newUser.setUlid(user_id);
            newUser.setEmail(email);
            newUser.setName(name);
            userService.insert(newUser);
            userInfo = userService.selectOneUser(email);
        }
        return userInfo;
    }
}
