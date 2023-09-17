package com.example.spring_project.presentation.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {
    
    @PostMapping(value = "/api/session")
    public String session(@RequestBody Map<String,Object> requestBody){
        System.out.println("session controller");
        return "session";
    }
    
}
