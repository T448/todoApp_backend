package com.example.spring_project.infrastructure.googleApi.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class DateAndTimezone {
    private String date;
    private String timeZone;
}
