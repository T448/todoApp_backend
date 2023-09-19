package com.example.spring_project.domain.repository;

import com.example.spring_project.domain.entity.Event;

import java.util.Date;
import java.util.List;

public interface EventRepository {
    public Number RegisterEvents(List<Event> eventList);

    public Date GetLatestUpdatedDate(String email);
}