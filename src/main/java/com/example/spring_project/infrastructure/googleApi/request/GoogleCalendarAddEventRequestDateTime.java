package com.example.spring_project.infrastructure.googleApi.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GoogleCalendarAddEventRequestDateTime {
    private String summary;
    private String description;
    private DatetimeAndTimezone start;
    private DatetimeAndTimezone end;

    public GoogleCalendarAddEventRequestDateTime(String summary, String description, String startDatetime,
            String endDatetime,
            String timezone) {
        this.summary = summary;
        this.description = description;
        this.start = new DatetimeAndTimezone(startDatetime, timezone);
        this.end = new DatetimeAndTimezone(endDatetime, timezone);
    }
}
