package com.example.spring_project.infrastructure.googleApi.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GoogleCalendarAddEventRequestDate {
    private String summary;
    private String description;
    private DateAndTimezone start;
    private DateAndTimezone end;

    public GoogleCalendarAddEventRequestDate(String summary, String description, String startDatetime,
            String endDatetime,
            String timezone) {
        this.summary = summary;
        this.description = description;
        this.start = new DateAndTimezone(startDatetime, timezone);
        this.end = new DateAndTimezone(endDatetime, timezone);
    }
}
