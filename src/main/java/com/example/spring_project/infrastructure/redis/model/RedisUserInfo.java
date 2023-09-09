package com.example.spring_project.infrastructure.redis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RedisUserInfo {
    String ulid;
    String email;
    String name;
    String accessToken;
    String refreshToken;
    String expires;
}
