package com.example.spring_project.infrastructure.rdb.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.example.spring_project.domain.entity.User;
@Mapper
public interface UserMapper {
  public boolean registerUser(User user);
  
  public ArrayList<User> selectByEmail(String email);
}
