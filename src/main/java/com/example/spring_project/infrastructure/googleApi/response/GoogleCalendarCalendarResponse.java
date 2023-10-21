package com.example.spring_project.infrastructure.googleApi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GoogleCalendarCalendarResponse {
    private String id;
    private String name;
    private String memo;
    private String colorId;
}
