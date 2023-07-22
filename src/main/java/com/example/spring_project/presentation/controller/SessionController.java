package com.example.spring_project.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import com.example.spring_project.usecase.SessionUsecase;;

@RestController
public class SessionController {
    @Autowired
    SessionUsecase sessionUsecase;

    @PostMapping(value = "/api/session", consumes = "application/json")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, Object> session(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap();
        response.put("message", "check session");
        return response;
    }
}
