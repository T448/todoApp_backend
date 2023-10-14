package com.example.spring_project.domain.repository;

import java.util.List;

import com.example.spring_project.domain.entity.Project;

public interface ProjectRepository {
    public void updateProject(String nameNew, String nameOld, String color_id, String memo, String email);

    public Project selectByNameAndEmail(String name, String email);

    public List<Project> selectByEmail(String email);

    public void insertProject(String id, String name, String color_id, String memo, String email);
}