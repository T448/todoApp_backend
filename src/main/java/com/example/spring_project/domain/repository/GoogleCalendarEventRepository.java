package com.example.spring_project.domain.repository;

public interface GoogleCalendarEventRepository {
        public String addNewEvent(String summary, String description, String startDate, String endDate,
                        String startDateTime, String endDateTime,
                        String timeZone, String calendarId, String accessToken);

        public String updateEvent(String eventId, String summary, String description, String startDate, String endDate,
                        String startDateTime,
                        String endDateTime,
                        String timeZone, String calendarId, String accessToken);

        public String deleteEvents(String calendarId, String eventId, String accessToken);
}
