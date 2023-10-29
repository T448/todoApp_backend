package com.example.spring_project.infrastructure.googleApi.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GoogleCalendarUpdateEventRequestDateTime {
    private String summary;
    private String description;
    private DatetimeAndTimezone startDateTime;
    private DatetimeAndTimezone endDateTime;

    public GoogleCalendarUpdateEventRequestDateTime(String summary, String description, String startDatetime,
            String endDatetime,
            String timezone) {
        this.summary = summary;
        this.description = description;
        this.startDateTime = new DatetimeAndTimezone(startDatetime, timezone);
        this.endDateTime = new DatetimeAndTimezone(endDatetime, timezone);
    }
}
