package com.example.spring_project.domain.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
// import org.apache.ibatis.annotations.Delete;
// import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;

import com.example.spring_project.domain.model.User;

@Mapper
public interface UserMapper {
    // 登録用メソッド
    @Insert("INSERT INTO users ( ulid, email, name ) VALUES ( #{ulid}, #{email}, #{name} )")
    public boolean insert(User user);

    // 検索用メソッド
    @Select("SELECT * FROM users WHERE email = #{email}")
    public List<User> selectMany(String email);

    // 検索用メソッド
    @Select("SELECT * FROM users WHERE email = #{email}")
    public User selectOneUser(String email);
}
