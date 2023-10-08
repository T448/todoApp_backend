package com.example.spring_project.domain.repository;

import com.example.spring_project.domain.entity.Project;

public interface ProjectRepository {
    public void updateProject(String nameNew,String nameOld,String color,String memo,String email);
    public Project selectByNameAndEmail(String name,String email);
}