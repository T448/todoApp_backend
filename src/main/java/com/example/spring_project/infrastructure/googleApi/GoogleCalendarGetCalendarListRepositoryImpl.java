package com.example.spring_project.infrastructure.googleApi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.spring_project.domain.repository.GoogleCalendarGetCalendarListRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GoogleCalendarGetCalendarListRepositoryImpl implements GoogleCalendarGetCalendarListRepository {
    private static final String REQUEST_URL = "https://www.googleapis.com/calendar/v3/users/me/calendarList";

    @Override
    public String getMainCalendarColor(String email, String accessToken) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(REQUEST_URL))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.body()).get("items");
            ArrayList<String> colorIdList = new ArrayList<>();
            node.forEach(item -> {
                if (item.get("id").toString().replaceAll("\"", "").equals(email)) {
                    colorIdList.add(item.get("colorId").toString().replaceAll("\"", ""));
                }
            });
            String colorId;
            System.out.println("colorIdList");
            System.out.println(colorIdList);
            if (colorIdList.isEmpty()) {
                colorId = "0";
            } else {
                colorId = colorIdList.get(0);
            }
            return colorId;

        } catch (Exception error) {
            System.out.println(error.toString());
            throw new IllegalArgumentException(error);
        }
    }
}
