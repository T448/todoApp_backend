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
    private DateAndTimezone startDate;
    private DateAndTimezone endDate;

    public GoogleCalendarUpdateEventRequestDate(String summary, String description, String startDate,
            String endDate,
            String timezone) {
        this.summary = summary;
        this.description = description;
        this.startDate = new DateAndTimezone(startDate, timezone);
        this.endDate = new DateAndTimezone(endDate, timezone);
    }
}
