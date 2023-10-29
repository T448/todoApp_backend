package com.example.spring_project.presentation.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UpdateEventRequest {
    private String eventId;
    private String name;
    private String memo;
    private String projectId;
    private String parentEventId;
    private String startDate;
    private String endDate;
    private String startDateTime;
    private String endDateTime;
    private String timeZone;

    public UpdateEventRequest() {

    }
}
