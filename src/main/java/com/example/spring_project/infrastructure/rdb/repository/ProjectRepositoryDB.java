package com.example.spring_project.infrastructure.rdb.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Project;
import com.example.spring_project.domain.repository.ProjectRepository;
import com.example.spring_project.infrastructure.rdb.mapper.ProjectMapper;

@Service
public class ProjectRepositoryDB implements ProjectRepository {
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public void updateProject(String nameNew, String nameOld, String color_id, String memo, String email) {
        projectMapper.updateProject(nameNew, nameOld, color_id, memo, email);
    }

    @Override
    public Project selectByNameAndEmail(String name, String email) {
        return projectMapper.selectByNameAndEmail(name, email);
    }

    @Override
    public List<Project> selectByEmail(String email) {
        return projectMapper.selectByEmail(email);
    }

    @Override
    public void insertProject(String id, String name, String color, String memo, String email) {
        projectMapper.insertProject(id, name, color, memo, email);
    }

    @Override
    public void updateProjectById(Project project) {
        projectMapper.updateProjectById(project);
    };

    @Override
    public void updateProjects(List<Project> projectList) {
        projectMapper.updateProjects(projectList);
    };

    @Override
    public void insertProjects(List<Project> project_list) {
        projectMapper.insertProjects(project_list);
    }

    @Override
    public void deleteProjects(List<Project> project_list) {
        projectMapper.deleteProjects(project_list);
    }
}
