package com.example.spring_project.infrastructure.googleApi;

import com.example.spring_project.common.methods.TimeCalculator;
import com.example.spring_project.config.ApplicationProperty;
import com.example.spring_project.domain.entity.User;
import com.example.spring_project.domain.repository.GoogleOauthRepository;
import com.example.spring_project.infrastructure.googleApi.response.GoogleOauthRefreshResponse;
import com.example.spring_project.infrastructure.googleApi.response.GoogleOauthResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleOauthRepositoryImpl implements GoogleOauthRepository {

  @Autowired
  private ApplicationProperty applicationProperty;

  @Override
  public GoogleOauthResponse GetAccessToken(String authCode) throws UnsupportedEncodingException {
    
    String requestUrl = applicationProperty.get("spring.oauth2_request_url");
    String clientId = applicationProperty.get("spring.client_id");
    String clientSecret = applicationProperty.get("spring.client_secret");
    String redirectUri = URLEncoder.encode(applicationProperty.get("spring.redirect_uri"),"UTF-8");

    String content = "";
    content += "code=" + authCode;
    content += "&client_id=" + clientId;
    content += "&client_secret=" + clientSecret;
    content += "&redirect_uri=" + redirectUri;
    content += "&grant_type=authorization_code";

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(requestUrl))
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("charset", "UTF-8")
      .POST(HttpRequest.BodyPublishers.ofString(content))
      .build();

    try {
      HttpResponse<String> responseFromGoogle = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );
      ObjectMapper mapper = new ObjectMapper();
      JsonNode node = mapper.readTree(responseFromGoogle.body());
      String access_token = node.get("access_token").textValue();
      String refresh_token = node.get("refresh_token").textValue();
      String expires_in = node.get("expires_in").toString();
      String id_token = node.get("id_token").textValue();

      GoogleOauthResponse googleOauthResponse = new GoogleOauthResponse(
        access_token,
        refresh_token,
        id_token,
        expires_in        
      );
      return googleOauthResponse;
    } catch (Exception error) {
      throw new IllegalArgumentException(error.toString());
    }
  }

  @Override
  public GoogleOauthRefreshResponse RefreshAccessToken(
    User user,
    String sessionId,
    String refreshToken
  ) throws UnsupportedEncodingException {
    String requestUrl = applicationProperty.get("spring.oauth2_request_url");
    String clientSecret = applicationProperty.get("spring.client_secret");
    String clientId = applicationProperty.get("spring.client_id");

    String content = "";
    content += "client_secret=" + URLEncoder.encode(clientSecret, "UTF-8");
    content += "&grant_type=" + URLEncoder.encode("refresh_token", "UTF-8");
    content += "&refresh_token=" + URLEncoder.encode(refreshToken, "UTF-8");
    content += "&client_id=" + URLEncoder.encode(clientId, "UTF-8");

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest requestNewAccessToken = HttpRequest
      .newBuilder()
      .uri(URI.create(requestUrl))
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("charset", "UTF-8")
      .POST(HttpRequest.BodyPublishers.ofString(content))
      .build();

    try {
      HttpResponse<String> responseNewAccessToken = client.send(
        requestNewAccessToken,
        HttpResponse.BodyHandlers.ofString()
      );
      ObjectMapper mapper = new ObjectMapper();
      JsonNode node = mapper.readTree(responseNewAccessToken.body());
      String updatedAccessToken = node.get("access_token").toString();
      String expiresIn = node.get("expires_in").toString();
      String updatedExpires = TimeCalculator.getTimeAfterSeconds(expiresIn);
      GoogleOauthRefreshResponse googleOauthRefreshResponse = new GoogleOauthRefreshResponse(
        updatedAccessToken,
        updatedExpires
      );
      // TODO
      // レスポンスをどうするか。
      // このメソッド内でどこまでやるか。
      return googleOauthRefreshResponse;
    } catch (Exception error) {
      throw new IllegalArgumentException(error.toString());
    }
  }
}
