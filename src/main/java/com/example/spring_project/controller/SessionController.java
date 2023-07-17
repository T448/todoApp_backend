package com.example.spring_project.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import redis.clients.jedis.Jedis;

import com.example.spring_project.domain.model.SessionException;;

@RestController
public class SessionController {
    @PostMapping(value = "/api/session", consumes = "application/json")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, Object> session(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap();
        Integer sessionStatus;
        String message;
        if (request.keySet().contains("sessionID") && request.get("sessionID") != null) {
            String sessionID = request.get("sessionID").toString();

            Jedis jedis = new Jedis("redis", 6379);
            List checkSessionResult = jedis.lrange(sessionID, 0, -1);
            System.out.println(checkSessionResult);
            jedis.close();

            if (checkSessionResult.size() == 0) {
                sessionStatus = 0;
                message = "session is not connected";
                throw new SessionException(message);
            } else {
                sessionStatus = 1;
                message = "session is connected!";
            }

        } else {
            sessionStatus = 0;
            message = "session is not connected";
            throw new SessionException(message);
        }
        response.put("session_status", sessionStatus);
        response.put("message", message);
        return response;
    }
}
