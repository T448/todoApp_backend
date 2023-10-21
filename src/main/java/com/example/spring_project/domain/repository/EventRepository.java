package com.example.spring_project.domain.repository;

import com.example.spring_project.domain.entity.Event;

import java.util.Date;
import java.util.List;

public interface EventRepository {
    public Number RegisterEvents(List<Event> eventList);

    public List<Event> GetEvents(String email, Date updated_at);

    public Date GetLatestUpdatedDate(String email);

    public void UpdateEvent(Event event);

    public void UpdateEvents(List<Event> event);

    public List<Event> selectByEmail(String email);

    public void deleteEvents(List<Event> eventList);
}
