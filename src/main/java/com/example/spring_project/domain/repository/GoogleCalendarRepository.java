package com.example.spring_project.domain.repository;

import java.util.List;
import java.util.Date;

import com.example.spring_project.domain.entity.Event;

public interface GoogleCalendarRepository {
    public List<Event> GetGoogleCalendarEvents(String email,String accessToken,Date updatedMin);
}
