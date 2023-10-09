package com.example.spring_project.service;

import java.util.Date;
import java.util.List;

import com.example.spring_project.domain.entity.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class EventDto {
    private String id;
    private String email;
    private String title;
    private String shortTitle;
    private String projectId;
    private String projectColor;
    private String parentEventId;
    private List<String> childEventIdList;
    private String memo;
    private Date start;
    private Date end;
    private Date createdAt;
    private Date updatedAt;

    public void setChildEventIdList(List<Event> eventList) {
        this.childEventIdList = eventList
                .stream()
                .filter(item -> item.getParent_event_id().equals(this.id))
                .map(item -> item.getId())
                .toList();
    }
}
