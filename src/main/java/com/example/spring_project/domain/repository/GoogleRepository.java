package com.example.spring_project.domain.repository;

import java.util.Map;

public interface GoogleRepository {
  public Map<String, String> GetUserInfo(String accessToken);
}
