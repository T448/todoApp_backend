package com.example.spring_project.infrastructure.googleApi.response;

import lombok.Getter;

@Getter
public class GoogleOauthRefreshResponse {

  String accessToken;
  String expires;

  public GoogleOauthRefreshResponse(String accessToken, String expires) {
    this.accessToken = accessToken;
    this.expires = expires;
  }
}
