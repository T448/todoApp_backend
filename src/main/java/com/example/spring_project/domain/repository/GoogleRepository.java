package com.example.spring_project.domain.repository;

import com.example.spring_project.infrastructure.googleApi.response.GoogleGetUserInfoResponse;

public interface GoogleRepository {
  public GoogleGetUserInfoResponse GetUserInfo(String accessToken);
}
