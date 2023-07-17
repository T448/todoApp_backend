package com.example.spring_project.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import com.example.spring_project.domain.model.BadRequestException;
import com.example.spring_project.domain.model.User;
import com.example.spring_project.domain.service.UserService;
import com.example.spring_project.usecase.SessionUsecase;

@RestController
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    SessionUsecase sessionUsecase;

    @PostMapping(value = "/api/login", consumes = "application/json")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, Object> login(@RequestBody(required = false) Map<String, Object> request) {

        Map<String, Object> response = new HashMap();

        if (request == null) {
            throw new BadRequestException("The request body is invalid.");
        } else {
            String email = request.get("email").toString();
            String name = request.get("name").toString();

            if (email.contains("@gmail.com") == false) {
                throw new BadRequestException("Use gmail!");
            } else {
                String sessionID = sessionUsecase.GenerateSession(email, name);
                response.put("sessionID", sessionID);
                User userInfo = userService.selectOneUser(email);
                response.put("user_info", userInfo);
            }
        }
        return response;
    }
}