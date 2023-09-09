package com.example.spring_project.infrastructure.rdb.repository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.User;
import com.example.spring_project.domain.repository.UserRepository;
import com.example.spring_project.infrastructure.rdb.mapper.UserMapper;

@Service
public class UserRepositoryDB implements UserRepository {

  @Autowired
  private UserMapper userMapper;

  @Override
  public String RegisterUser(User user) {
    try {
      userMapper.registerUser(user);
      return user.getUlid();
    } catch (Exception error) {
      return error.toString();
    }
  }

  @Override
  public ArrayList<User> SelectByEmail(String email) {
    return userMapper.selectByEmail(email);
  }
}
