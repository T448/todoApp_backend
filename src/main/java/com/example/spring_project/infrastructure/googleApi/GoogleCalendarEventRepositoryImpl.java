package com.example.spring_project.infrastructure.googleApi;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.example.spring_project.domain.repository.GoogleCalendarEventRepository;
import com.example.spring_project.infrastructure.googleApi.request.GoogleCalendarAddEventRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GoogleCalendarEventRepositoryImpl implements GoogleCalendarEventRepository {

    @Override
    public String addNewEvent(String summary, String description, String startDateTime, String endDateTime,
            String timeZone, String calendarId, String accessToken) {
        String requestUrl = "";
        try {
            requestUrl = "https://www.googleapis.com/calendar/v3/calendars/"
                    + URLEncoder.encode(calendarId, "UTF-8")
                    + "/events";
        } catch (Exception e) {
            log.error(e.toString());
        }

        var newEvent = new GoogleCalendarAddEventRequest(summary, description, startDateTime, endDateTime, timeZone);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String requestBody = "";
        try {
            requestBody = objectMapper.writeValueAsString(newEvent);
        } catch (Exception e) {
            log.error(e.toString());
        }
        log.info("イベント追加のrequestBody");
        log.info(requestBody);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(requestUrl))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .header("charset", "UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        if (!requestUrl.isEmpty()) {
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                log.info(response.toString());
                ObjectMapper responseObjectMapper = new ObjectMapper();
                JsonNode responseNode = responseObjectMapper.readTree(response.body());
                if (responseNode.has("id")) {
                    return responseNode.get("id").toString().replaceAll("\"", "");
                } else {
                    return responseNode.get("error").get("message").toString();
                }
            } catch (Exception e) {
                log.error(e.toString());
                throw new IllegalArgumentException(e.toString());
            }
        } else {
            log.error("calendarIDが不適切");
            throw new IllegalArgumentException("calendarIDが不適切");
        }
    }
}
