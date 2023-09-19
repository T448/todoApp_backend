package com.example.spring_project.presentation.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_project.domain.entity.Event;
import com.example.spring_project.domain.repository.EventRepository;
import com.example.spring_project.domain.repository.GoogleCalendarRepository;
import com.example.spring_project.presentation.HideValue;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class EventController {

    private final HideValue hideValue;

    GoogleCalendarRepository googleCalendarRepository;
    EventRepository eventRepository;
    
    @GetMapping(value = "/api/events")
    public String getEvents(){
        try{
            String accessToken = hideValue.getHideTokenValue();
            String email = hideValue.getHideEmailValue();

            String dateIniStr = "2010-01-01 00:00:00";
            
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date dateIni = sdFormat.parse(dateIniStr);
            // TODO : 前回取得時刻以降のみのフィルターを使えるようにする
            // TODO: イベントのタグの扱いについて調べる
            List<Event> eventList = googleCalendarRepository.GetGoogleCalendarEvents(email, accessToken, dateIni);
            String registerResult = eventRepository.RegisterEvents(eventList);
            return registerResult;
        } catch(Exception e){
            throw new Error(e.toString());
        }
    }
}
