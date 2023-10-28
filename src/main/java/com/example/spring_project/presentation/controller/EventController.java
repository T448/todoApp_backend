package com.example.spring_project.presentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_project.presentation.HideValue;
import com.example.spring_project.presentation.model.request.DeleteEventsRequest;
import com.example.spring_project.presentation.model.request.EventRequest;
import com.example.spring_project.presentation.model.request.UpdateEventRequest;
import com.example.spring_project.service.EventDto;
import com.example.spring_project.service.EventUsecase;
import com.example.spring_project.service.Synchronize;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class EventController {
    @Autowired
    Synchronize suSynchronize;
    private final HideValue hideValue;
    private EventUsecase eventUsecase;

    @GetMapping(value = "/api/events")
    public List<EventDto> getEvents(@RequestParam(required = false) Boolean all) {
        try {
            String accessToken = hideValue.getHideTokenValue();
            String email = hideValue.getHideEmailValue();
            log.info("---------------[event controller] getEvents---------------");
            log.info(String.valueOf(all));
            log.info(email.toString());
            if (all == null) {
                all = false;
            }

            // TODO : 前回取得時刻以降のみのフィルターを使えるようにする
            // TODO: イベントのタグの扱いについて調べる
            List<EventDto> registerResult = eventUsecase.getEvents(accessToken, email, all);
            return registerResult;

        } catch (Exception e) {
            throw new Error(e.toString());
        }
    }

    @PostMapping(value = "/api/events")
    public String addEvents(@RequestBody EventRequest request) {
        String email = hideValue.getHideEmailValue();
        String accessToken = hideValue.getHideTokenValue();
        String newEventId = eventUsecase.addEvents(request.getName(), request.getMemo(), request.getProjectId(),
                request.getParentEventId(), request.getStartDateTime(), request.getEndDateTime(), request.getTimeZone(),
                accessToken, email);
        return newEventId;
    }

    @PutMapping(value = "api/events")
    public String updateEvents(@RequestBody UpdateEventRequest request) {
        String email = hideValue.getHideEmailValue();
        String accessToken = hideValue.getHideTokenValue();
        return eventUsecase.updateEvent(request.getEventId(), request.getName(), request.getMemo(),
                request.getProjectId(),
                request.getStartDateTime(), request.getEndDateTime(), request.getTimeZone(), accessToken, email);
    }

    @DeleteMapping(value = "api/events")
    public String deleteEvents(@RequestBody DeleteEventsRequest request) {
        String email = hideValue.getHideEmailValue();
        String accessToken = hideValue.getHideTokenValue();
        String deleteEventsResponse = eventUsecase.deleteEvents(request.getEventIdList(), email, request.getProjectId(),
                accessToken);
        return deleteEventsResponse;
    }

    @GetMapping(value = "api/synchronize")
    public String synchronize() {
        String email = hideValue.getHideEmailValue();
        String accessToken = hideValue.getHideTokenValue();
        suSynchronize.synchronize(email, accessToken, true, true, true);
        return "";
    }
}
