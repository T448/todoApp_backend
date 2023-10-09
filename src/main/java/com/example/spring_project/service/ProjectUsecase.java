package com.example.spring_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.repository.ProjectRepository;

import de.huxhorn.sulky.ulid.ULID;

@Service
public class ProjectUsecase {
    @Autowired
    ProjectRepository projectRepository;

    public void addNewProject(String name, String color, String memo, String email) {
        ULID ulid = new ULID();
        projectRepository.insertProject(ulid.nextULID(), name, color, memo, email);
    }
}
