package com.example.spring_project.infrastructure.googleApi;

import com.example.spring_project.config.ApplicationProperty;
import com.example.spring_project.domain.repository.GoogleRepository;
import com.example.spring_project.infrastructure.googleApi.response.GoogleGetUserInfoResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleRepositoryImpl implements GoogleRepository {

  @Autowired
  private ApplicationProperty applicationProperty;

  @Override
  public GoogleGetUserInfoResponse GetUserInfo(String accessToken) {
    
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
      String email = node.get("email").toString();
      String name = node.get("name").toString();
      GoogleGetUserInfoResponse response = new GoogleGetUserInfoResponse(email, name);
      return response;
    } catch (IOException | InterruptedException e) {
      throw new Error(e.toString());
    }
  }
}
