package com.example.spring_project.infrastructure.googleApi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.example.spring_project.domain.repository.GoogleCalendarCalendarRepository;
import com.example.spring_project.infrastructure.googleApi.request.GoogleCalendarAddCalendarRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GoogleCalendarCalendarRepositoryImpl implements GoogleCalendarCalendarRepository {
    private static final String REQUEST_URL = "https://www.googleapis.com/calendar/v3/calendars";

    @Override
    public String addNewCalendar(String email, String accessToken, String name, String memo) {

        var newCalendar = new GoogleCalendarAddCalendarRequest(name, memo);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String requestBody = "";
        try {
            requestBody = objectMapper.writeValueAsString(newCalendar);
        } catch (Exception e) {
            log.error(e.toString());
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(REQUEST_URL))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .header("charset", "UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
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
    }
}
