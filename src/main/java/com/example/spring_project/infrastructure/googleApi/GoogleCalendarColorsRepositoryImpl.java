package com.example.spring_project.infrastructure.googleApi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Color;
import com.example.spring_project.domain.repository.GoogleCalendarColorsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GoogleCalendarColorsRepositoryImpl implements GoogleCalendarColorsRepository {
    private static final String REQUEST_URL = "https://www.googleapis.com/calendar/v3/colors";

    @Override
    public List<Color> getGoogleCalendarColors(String email, String accessToken) {
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
            JsonNode node = mapper.readTree(response.body()).get("calendar");
            ArrayList<Color> colorList = new ArrayList<>();
            AtomicInteger indexHolder = new AtomicInteger();
            node.forEach(item -> {
                colorList
                        .add(new Color(
                                String.valueOf(indexHolder.getAndIncrement()),
                                item.get("background").toString().replaceAll("\"", ""),
                                email));
            });

            return colorList;
        } catch (Exception error) {
            // System.out.println(error.toString());
            log.error(error.toString());
            throw new IllegalArgumentException(error);
        }
    }
}
