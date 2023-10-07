package com.example.spring_project.infrastructure.rdb.repository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.User;
import com.example.spring_project.domain.repository.UserRepository;
import com.example.spring_project.infrastructure.rdb.mapper.ProjectMapper;
import com.example.spring_project.infrastructure.rdb.mapper.UserMapper;

import de.huxhorn.sulky.ulid.ULID;

@Service
public class UserRepositoryDB implements UserRepository {

  @Autowired
  private UserMapper userMapper;
  @Autowired
  private ProjectMapper projectMapper;

  @Override
  public String RegisterUser(User user) {
    try {
      ULID ulid = new ULID();
      userMapper.registerUser(user);
      projectMapper.insertProject(ulid.nextULID(), "General", "#00ff00", "", user.getEmail());
      return user.getUlid();
    } catch (Exception error) {
      System.out.println(error.toString());
      return error.toString();
    }
  }

  @Override
  public ArrayList<User> SelectByEmail(String email) {
    return userMapper.selectByEmail(email);
  }
}
