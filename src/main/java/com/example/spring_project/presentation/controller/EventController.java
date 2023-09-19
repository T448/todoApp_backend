package com.example.spring_project.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_project.presentation.HideValue;
import com.example.spring_project.service.EventUsecase;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class EventController {

    private final HideValue hideValue;
    private EventUsecase eventUsecase;
    
    @GetMapping(value = "/api/events")
    public Number getEvents(){
        try{
            String accessToken = hideValue.getHideTokenValue();
            String email = hideValue.getHideEmailValue();

     
            // TODO : 前回取得時刻以降のみのフィルターを使えるようにする
            // TODO: イベントのタグの扱いについて調べる
            Number registerResult = eventUsecase.getEvents(accessToken, email);
            return registerResult;
            
        } catch(Exception e){
            throw new Error(e.toString());
        }
    }
}
