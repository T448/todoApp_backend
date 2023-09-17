package com.example.spring_project.infrastructure.redis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedisUserInfo {
    private String ulid;
    private String email;
    private String name;
    private String accessToken;
    private String refreshToken;
    private String expires;

    public RedisUserInfo(){}

}
