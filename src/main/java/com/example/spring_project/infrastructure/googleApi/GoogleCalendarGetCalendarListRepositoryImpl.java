package com.example.spring_project.infrastructure.googleApi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Project;
import com.example.spring_project.domain.repository.GoogleCalendarGetCalendarListRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoogleCalendarGetCalendarListRepositoryImpl implements GoogleCalendarGetCalendarListRepository {
    private static final String REQUEST_URL = "https://www.googleapis.com/calendar/v3/users/me/calendarList";

    @Override
    public List<Project> getCalendarList(String email, String accessToken) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(REQUEST_URL))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        try {
            log.info("GoogleCalendarGetCalendarListRepositoryImpl");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node0 = mapper.readTree(response.body());
            log.info("node0");
            log.info(node0.toString());
            var node = node0.get("items");
            log.info("node");
            log.info(node.toString());
            ArrayList<Project> calendarList = new ArrayList<>();
            node.forEach(item -> {
                var memo = "";
                if (item.get("description") != null) {
                    memo = item.get("description").toString().replaceAll("\"", "");
                }
                calendarList.add(
                        new Project(
                                item.get("id").toString().replaceAll("\"", ""), "General",
                                item.get("colorId").toString().replaceAll("\"", ""),
                                memo,
                                email,
                                null,
                                null));
            });
            log.info(calendarList.toString());
            return calendarList;
        } catch (Exception error) {
            log.error(error.toString());
            throw new IllegalArgumentException(error);
        }
    }
}
