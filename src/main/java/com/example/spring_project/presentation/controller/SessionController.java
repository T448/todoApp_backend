package com.example.spring_project.presentation.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class SessionController {

    @PostMapping(value = "/api/session")
    public String session(@RequestBody Map<String, Object> requestBody) {
        log.info("-----[session controller]-----");

        return "session";
    }

}
