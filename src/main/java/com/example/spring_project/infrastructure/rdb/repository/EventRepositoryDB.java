package com.example.spring_project.infrastructure.rdb.repository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Event;
import com.example.spring_project.domain.repository.EventRepository;
import com.example.spring_project.infrastructure.rdb.mapper.EventMapper;

@Service
public class EventRepositoryDB implements EventRepository {
    
    @Autowired
    private EventMapper eventMapper;

    @Override
    public Number RegisterEvents(List<Event> eventList){
        try{
            eventMapper.registerEvents(eventList);
            return eventList.size();
        } catch (Exception error){
            System.out.println(error);
            return -1;
        }
    }

    @Override
    public Date GetLatestUpdatedDate(String email){
        return eventMapper.getLatestUpdatedDate(email);
    }
}