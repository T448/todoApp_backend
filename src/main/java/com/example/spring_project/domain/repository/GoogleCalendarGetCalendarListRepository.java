package com.example.spring_project.domain.repository;

import java.util.List;

import com.example.spring_project.domain.entity.Project;

public interface GoogleCalendarGetCalendarListRepository {
    List<Project> getCalendarList(String email, String accessToken);
}
