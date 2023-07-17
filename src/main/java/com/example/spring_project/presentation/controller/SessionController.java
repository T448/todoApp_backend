package com.example.spring_project.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.spring_project.domain.model.ForbiddenException;
import com.example.spring_project.usecase.SessionUsecase;;

@RestController
public class SessionController {
    @Autowired
    SessionUsecase sessionUsecase;

    @PostMapping(value = "/api/session", consumes = "application/json")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, Object> session(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap();
        if (request.keySet().contains("sessionID") && request.get("sessionID") != null) {
            String sessionID = request.get("sessionID").toString();

            List<?> checkSessionResult = sessionUsecase.CheckSession(sessionID);
            response.put("sessionID", sessionID);
            response.put("user_info", checkSessionResult);
            if (checkSessionResult.size() == 0) {
                throw new ForbiddenException("session is not connected");
            }
        } else {
            throw new ForbiddenException("session is not connected");
        }
        return response;
    }
}
