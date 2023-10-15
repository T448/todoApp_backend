package com.example.spring_project.domain.repository;

public interface GoogleCalendarCalendarRepository {
    public String addNewCalendar(String email, String accessToken, String name, String memo);
}
