package com.example.spring_project.domain.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Event {
    private String id;
    private String email;
    private String title;
    private String short_title;
    private String project_id;
    private String parent_event_id;
    private String memo;
    private Date startDate;
    private Date endDate;
    private Date startDateTime;
    private Date endDateTime;
    private Date created_at;
    private Date updated_at;

    public void setProjectId(String newProjectIdId) {
        this.project_id = newProjectIdId;
    }
}
