package com.example.spring_project.infrastructure.rdb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.spring_project.domain.entity.MemoTemplate;

@Mapper
public interface MemoTemplateMapper {
    public List<MemoTemplate> selectByEmail(String user_email);

    public void insert(
            @Param("id") String id,
            @Param("name") String name,
            @Param("template") String template,
            @Param("user_email") String user_email);

    public void update(
            @Param("id") String id,
            @Param("name") String name,
            @Param("template") String template);
}
