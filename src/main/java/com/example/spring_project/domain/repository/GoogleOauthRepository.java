package com.example.spring_project.domain.repository;

import com.example.spring_project.domain.entity.User;
import com.example.spring_project.infrastructure.googleApi.response.GoogleOauthRefreshResponse;
import com.example.spring_project.infrastructure.googleApi.response.GoogleOauthResponse;
import java.io.UnsupportedEncodingException;

public interface GoogleOauthRepository {
  public GoogleOauthResponse GetAccessToken(String content);

  public GoogleOauthRefreshResponse RefreshAccessToken(
    User user,
    String sessionId,
    String refreshToken
  ) throws UnsupportedEncodingException;
}
