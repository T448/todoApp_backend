package com.example.spring_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Project;
import com.example.spring_project.domain.repository.GoogleCalendarCalendarRepository;
import com.example.spring_project.domain.repository.ProjectRepository;
import com.example.spring_project.presentation.HideValue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ProjectUsecase {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    GoogleCalendarCalendarRepository googleCalendarCalendarRepository;

    private final HideValue hideValue;

    public String addNewProject(String name, String memo, String email) {
        String accessToken = hideValue.getHideTokenValue();
        String newCalendarId = googleCalendarCalendarRepository.addNewCalendar(email, accessToken, name, memo);
        log.info(newCalendarId);
        if (newCalendarId.startsWith("[error]", 0)) {
            return "";
        }
        Project projectDetail = googleCalendarCalendarRepository.getCalendarById(newCalendarId, accessToken, email);
        projectRepository.insertProject(newCalendarId, name, projectDetail.getColor_id(), memo, email);
        return newCalendarId;
    }
}
