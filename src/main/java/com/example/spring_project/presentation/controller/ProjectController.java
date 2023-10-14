package com.example.spring_project.presentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_project.domain.entity.Project;
import com.example.spring_project.domain.repository.ProjectRepository;
import com.example.spring_project.presentation.HideValue;
import com.example.spring_project.presentation.model.request.ProjectRequest;
import com.example.spring_project.service.ProjectUsecase;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class ProjectController {
    private final HideValue hideValue;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectUsecase projectUsecase;

    @GetMapping(value = "/api/projects")
    public List<Project> getProjects() {
        String email = hideValue.getHideEmailValue();
        List<Project> projectList = projectRepository.selectByEmail(email);
        return projectList;
    }

    @PostMapping(value = "/api/project")
    public void postProject(@RequestBody ProjectRequest request) {
        System.out.println("-----[POST project request]-----");
        System.out.println(request);
        String email = hideValue.getHideEmailValue();
        projectUsecase.addNewProject(
                request.getName(),
                request.getColor(),
                request.getMemo(),
                email);
    }

}
