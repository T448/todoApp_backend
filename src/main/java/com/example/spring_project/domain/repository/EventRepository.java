package com.example.spring_project.domain.repository;

import com.example.spring_project.domain.entity.Event;
import java.util.List;

public interface EventRepository {
    public String RegisterEvents(List<Event> eventList);
}
