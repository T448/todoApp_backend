package com.example.spring_project.infrastructure.googleApi.response;

import lombok.Getter;

@Getter
public class GoogleOauthResponse {

  String accessToken;
  String refreshToken;
  String idToken;
  String expiresIn;

  public GoogleOauthResponse(
    String accessToken,
    String refreshToken,
    String idToken,
    String expiresIn
  ) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.idToken = idToken;
    this.expiresIn = expiresIn;
  }
}
