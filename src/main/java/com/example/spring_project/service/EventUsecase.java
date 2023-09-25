package com.example.spring_project.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Event;
import com.example.spring_project.domain.repository.EventRepository;
import com.example.spring_project.domain.repository.GoogleCalendarRepository;

@Service
public class EventUsecase {

    @Autowired
    GoogleCalendarRepository googleCalendarRepository;
    @Autowired
    EventRepository eventRepository;

    public Number getEvents(String accessToken,String email){
        Date latestUpdatedDate = eventRepository.GetLatestUpdatedDate(email);
        System.out.println("---------------最終更新---------------");
        System.out.println(latestUpdatedDate);
        try{   
            List<Event> eventList = googleCalendarRepository.GetGoogleCalendarEvents(email, accessToken, latestUpdatedDate);
            Number registerResult = eventRepository.RegisterEvents(eventList);
            System.out.println("---------------DBに新たに登録された件数---------------");
            System.out.println(registerResult);
            return registerResult;
        } catch(Exception e){
            throw new Error(e.toString());
        }
    }
}
