package com.example.spring_project.presentation.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_project.domain.entity.Project;
import com.example.spring_project.domain.repository.ProjectRepository;
import com.example.spring_project.presentation.HideValue;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class ProjectController {
    private final HideValue hideValue;

    @Autowired
    ProjectRepository projectRepository;

    @GetMapping(value = "/api/projects")
    public List<Project> getProjects() {
        String email = hideValue.getHideEmailValue();
        List<Project> projectList = projectRepository.selectByEmail(email);
        return projectList;
    }

    @PostMapping(value = "/api/project")
    public String postProject(@RequestBody String request) {
        return request;
    }

}
