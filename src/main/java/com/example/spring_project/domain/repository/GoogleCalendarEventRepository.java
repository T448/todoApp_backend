package com.example.spring_project.domain.repository;

public interface GoogleCalendarEventRepository {
    public String addNewEvent(String summary, String description, String startDateTime, String endDateTime,
            String timeZone, String calendarId, String accessToken);
}
