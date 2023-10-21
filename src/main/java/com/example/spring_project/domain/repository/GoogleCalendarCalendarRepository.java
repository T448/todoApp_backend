package com.example.spring_project.domain.repository;

import java.util.List;

import com.example.spring_project.domain.entity.Project;

public interface GoogleCalendarCalendarRepository {
    public String addNewCalendar(String email, String accessToken, String name, String memo);

    public Project getCalendarById(String id, String accessToken, String email);

    public List<Project> getCalendarList(String accessToken, String email);
}
