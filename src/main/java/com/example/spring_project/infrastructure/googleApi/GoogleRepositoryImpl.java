package com.example.spring_project.infrastructure.googleApi;

import com.example.spring_project.config.ApplicationProperty;
import com.example.spring_project.domain.repository.GoogleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class GoogleRepositoryImpl implements GoogleRepository {

  @Autowired
  private ApplicationProperty applicationProperty;

  @Override
  public Map<String, String> GetUserInfo(String accessToken) {
    Map<String, String> response = new HashMap();
    String requestUrl = applicationProperty.get(
      "spring.get_user_info_request_url"
    );
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(requestUrl))
      .header("Authorization", "Bearer " + accessToken)
      .GET()
      .build();
    try {
      HttpResponse<String> response_from_google = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );
      ObjectMapper mapper = new ObjectMapper();
      JsonNode node = mapper.readTree(response_from_google.body());
      response.put("email", node.get("email").toString());
      response.put("name", node.get("name").toString());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return response;
  }
}
