package com.example.spring_project.domain.repository;

import java.util.ArrayList;

import com.example.spring_project.domain.entity.User;

public interface UserRepository {
  public String RegisterUser(User user,String color);

  public ArrayList<User> SelectByEmail(String email);
}
