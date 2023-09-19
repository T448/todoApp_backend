package com.example.spring_project.domain.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Event {
    private String id;
    private String email;
    private String summary;
    private String parent_event;
    private Date start;
    private Date end;
    private Date created_at;
    private Date updated_at;
}
