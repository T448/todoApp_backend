package com.example.spring_project.infrastructure.rdb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Project;
import com.example.spring_project.domain.repository.ProjectRepository;
import com.example.spring_project.infrastructure.rdb.mapper.ProjectMapper;

@Service
public class ProjectRepositoryDB implements ProjectRepository{
    @Autowired
    private ProjectMapper projectMapper;
    
    @Override
    public void updateProject(String nameNew,String nameOld,String color,String memo,String email){
        projectMapper.updateProject(nameNew, nameOld, color, memo, email);
    }

    @Override
    public Project selectByNameAndEmail(String name,String email){
        return projectMapper.selectByNameAndEmail(name, email);
    }
}
