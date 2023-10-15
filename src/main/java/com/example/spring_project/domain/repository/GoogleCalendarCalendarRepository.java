package com.example.spring_project.domain.repository;

import com.example.spring_project.domain.entity.Project;

public interface GoogleCalendarCalendarRepository {
    public String addNewCalendar(String email, String accessToken, String name, String memo);

    public Project getCalendarById(String id, String accessToken, String email);
}
