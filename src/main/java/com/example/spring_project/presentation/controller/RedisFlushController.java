package com.example.spring_project.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_project.infrastructure.redis.methods.RedisMethods;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RedisFlushController {

    @Autowired
    RedisMethods redisMethods;

    @PostMapping(value = "/api/redis-flush")
    public String redisFlush(String inputValue) {
        log.info("redisFlush");

        return redisMethods.DeleteAllData();
    }
}
