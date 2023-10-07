package com.example.spring_project.infrastructure.redis.repository;

import com.example.spring_project.domain.entity.User;
import com.example.spring_project.domain.repository.SessionRepository;
import com.example.spring_project.infrastructure.redis.methods.RedisMethods;
import com.example.spring_project.infrastructure.redis.model.RedisUserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisRepository implements SessionRepository {

  @Autowired
  private RedisMethods redisMethods;

  @Override
  public String GenerateSession(
    User user,
    String access_token,
    String refresh_token,
    String expires
  ) {
    String sessionID = UUID.randomUUID().toString();
    RedisUserInfo redisUserInfo = new RedisUserInfo(user.getUlid(), user.getEmail(), user.getName(), access_token.replaceAll("\"",""), refresh_token.replaceAll("\"",""), expires);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    try {
      String  redisUserInfoJsonString = objectMapper.writeValueAsString(redisUserInfo);
      String registerResult = redisMethods.RegisterString(
        sessionID,
        redisUserInfoJsonString
      );
      if (registerResult == sessionID) {
        return sessionID;
      } else {
        throw new Error("Session Error on Redis!");
      }
    } catch (JsonProcessingException error) {
      throw new Error(error.toString());
    }

    // String[] userInfoRedis = new String[6];
    // userInfoRedis[0] = user.getUlid();
    // userInfoRedis[1] = user.getEmail();
    // userInfoRedis[2] = user.getName();
    // userInfoRedis[3] = access_token;
    // userInfoRedis[4] = refresh_token;
    // userInfoRedis[5] = expires_in;


  }

  @Override
  public String CheckSession(String SessionID) {
    return redisMethods.GetString(SessionID);
  }

  @Override
  public String RefreshAccessToken(String sessionID, String newData) {
    return redisMethods.OverwriteDataString(sessionID, newData);
  }
}
