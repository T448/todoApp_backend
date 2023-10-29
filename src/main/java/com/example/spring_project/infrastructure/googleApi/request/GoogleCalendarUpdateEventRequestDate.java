package com.example.spring_project.infrastructure.googleApi.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GoogleCalendarUpdateEventRequestDate {
    private String summary;
    private String description;
    private DateAndTimezone start;
    private DateAndTimezone end;

    public GoogleCalendarUpdateEventRequestDate(String summary, String description, String startDate,
            String endDate,
            String timezone) {
        this.summary = summary;
        this.description = description;
        this.start = new DateAndTimezone(startDate, timezone);
        this.end = new DateAndTimezone(endDate, timezone);
    }
}
