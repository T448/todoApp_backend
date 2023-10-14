package com.example.spring_project.presentation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_project.domain.entity.Project;
import com.example.spring_project.domain.repository.GoogleCalendarGetCalendarListRepository;
import com.example.spring_project.domain.repository.ProjectRepository;
import com.example.spring_project.presentation.HideValue;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class SessionController {
    private final HideValue hideValue;
    private final static String GENERAL = "General";
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    private GoogleCalendarGetCalendarListRepository googleCalendarGetCalendarListRepository;

    @PostMapping(value = "/api/session")
    public String session(@RequestBody Map<String, Object> requestBody) {
        System.out.println("-----[session controller]-----");
        String email = hideValue.getHideEmailValue();
        String accessToken = hideValue.getHideTokenValue();

        Project projectBeforeUpdate = projectRepository.selectByNameAndEmail(GENERAL, email);
        String memoBeforeUpdate = projectBeforeUpdate.getMemo();
        List<Project> calendarList = googleCalendarGetCalendarListRepository.getCalendarList(email, accessToken);
        List<Project> mainCalendarList = calendarList
                .stream()
                .filter(item -> item.getId().equals(email))
                .toList();
        if (!mainCalendarList.isEmpty()) {
            projectRepository.updateProject(GENERAL, GENERAL, mainCalendarList.get(0).getColor_id(), memoBeforeUpdate,
                    email);
        }

        return "session";
    }

}
