package com.example.spring_project.infrastructure.rdb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.spring_project.domain.entity.Project;

@Mapper
public interface ProjectMapper {
    public boolean insertProject(
        @Param("id") String id,
        @Param("name") String name,
        @Param("color") String color,
        @Param("memo") String memo,
        @Param("email") String email
    );
    public void updateProject(
        @Param("name_new") String nameNew,
        @Param("name_old") String nameOld,
        @Param("color") String color,
        @Param("memo") String memo,
        @Param("email") String email
    );
    public Project selectByNameAndEmail(
        @Param("name") String name,
        @Param("email") String email
    );
    public List<Project> selectByEmail(
        @Param("email") String email
    );
}