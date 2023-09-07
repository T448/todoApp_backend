package com.example.spring_project.infrastructure.rdb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.spring_project.domain.entity.User;

@Mapper
public interface UserMapper {
  public boolean registerUser(@Param("user") User user);
  
  public User[] selectByEmail(@Param("email") String email);
}
