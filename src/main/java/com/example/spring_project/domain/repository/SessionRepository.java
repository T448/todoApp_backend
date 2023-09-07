package com.example.spring_project.domain.repository;

import java.util.List;

import com.example.spring_project.domain.entity.User;

public interface SessionRepository {
  public String GenerateSession(
    User user,
    String access_token,
    String refresh_token,
    String expires_in
  );

  public List<?> CheckSession(String sessionID);

  public String RefreshAccessToken(String sessionID, String[] newData);
}
