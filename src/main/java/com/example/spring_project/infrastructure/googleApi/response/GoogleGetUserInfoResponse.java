package com.example.spring_project.infrastructure.googleApi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleGetUserInfoResponse {
    String email;
    String name;
}
