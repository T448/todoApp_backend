package com.example.spring_project.domain.repository;

import com.example.spring_project.domain.entity.User;

public interface UserRepository {
  public String RegisterUser(User user);

  public User[] SelectByEmail(String email);
}
