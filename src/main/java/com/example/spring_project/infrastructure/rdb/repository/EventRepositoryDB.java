package com.example.spring_project.infrastructure.rdb.repository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Event;
import com.example.spring_project.domain.repository.EventRepository;
import com.example.spring_project.infrastructure.rdb.mapper.EventMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventRepositoryDB implements EventRepository {

    @Autowired
    private EventMapper eventMapper;

    @Override
    public Number RegisterEvents(List<Event> eventList) {
        try {
            eventMapper.registerEvents(eventList);
            return eventList.size();
        } catch (Exception error) {
            log.error(error.toString());
            return -1;
        }
    }

    @Override
    public List<Event> GetEvents(String email, Date update_at) {
        try {
            System.out.println("取得の直前");
            System.out.println(update_at);
            return eventMapper.getEvents(email, update_at);
        } catch (Exception error) {
            System.out.println("取得の直前でエラー");
            log.error(error.toString());
            throw new IllegalArgumentException(error);
        }
    }

    @Override
    public Date GetLatestUpdatedDate(String email) {
        return eventMapper.getLatestUpdatedDate(email);
    }

    @Override
    public void UpdateEvent(Event event) {
        eventMapper.updateEvent(event);
    }

    @Override
    public void UpdateEvents(List<Event> eventList) {
        eventMapper.updateEvents(eventList);
    }

    @Override
    public List<Event> selectByEmail(String email) {
        return eventMapper.selectByEmail(email);
    }

    @Override
    public void deleteEvents(List<Event> eventList) {
        eventMapper.deleteEvents(eventList);
    }
}
