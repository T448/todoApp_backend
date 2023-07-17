package com.example.spring_project.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_project.domain.model.User;
import com.example.spring_project.domain.repository.UserMapper;

@Transactional
@Service
public class UserService {
    @Autowired
    UserMapper todoAppUserMapper;

    // 登録用メソッド
    public boolean insert(User todoAppUser) {
        return todoAppUserMapper.insert(todoAppUser);
    }

    // 検索用メソッド
    public List<User> selectMany(String email) {
        return todoAppUserMapper.selectMany(email);
    }

    // 検索用メソッド
    public User selectOneUser(String email) {
        return todoAppUserMapper.selectOneUser(email);
    }
}
