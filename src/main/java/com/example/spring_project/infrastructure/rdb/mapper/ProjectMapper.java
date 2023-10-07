package com.example.spring_project.infrastructure.rdb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectMapper {
    public boolean insertProject(
        @Param("id") String id,
        @Param("name") String name,
        @Param("color") String color,
        @Param("memo") String memo,
        @Param("email") String email);
}