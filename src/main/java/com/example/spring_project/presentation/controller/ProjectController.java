package com.example.spring_project.presentation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_project.domain.entity.Color;
import com.example.spring_project.domain.entity.Project;
import com.example.spring_project.domain.repository.ColorRepository;
import com.example.spring_project.domain.repository.ProjectRepository;
import com.example.spring_project.presentation.HideValue;
import com.example.spring_project.presentation.controller.Response.GetProjectResponse;
import com.example.spring_project.presentation.model.request.ProjectRequest;
import com.example.spring_project.service.ProjectUsecase;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@RestController
@Slf4j
public class ProjectController {
    private final HideValue hideValue;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectUsecase projectUsecase;

    @Autowired
    ColorRepository colorRepository;

    @GetMapping(value = "/api/projects")
    public List<GetProjectResponse> getProjects() {
        String email = hideValue.getHideEmailValue();
        List<Project> projectList = projectRepository.selectByEmail(email);
        List<Color> colorList = colorRepository.selectByEmail(email);
        Map<String, String> colorIdAndCodeMap = new HashMap<String, String>();
        colorList.forEach(item -> {
            colorIdAndCodeMap.put(item.getId(), item.getCode());
        });
        return projectList
                .stream()
                .map(item -> new GetProjectResponse(item,
                        colorIdAndCodeMap))
                .toList();
    }

    @PostMapping(value = "/api/project")
    public void postProject(@RequestBody ProjectRequest request) {
        log.info("-----[POST project request]-----");
        log.info(request.toString());
        String email = hideValue.getHideEmailValue();
        projectUsecase.addNewProject(
                request.getName(),
                request.getMemo(),
                email);
    }

}
