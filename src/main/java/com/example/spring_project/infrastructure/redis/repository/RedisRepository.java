package com.example.spring_project.infrastructure.redis.repository;

import com.example.spring_project.domain.entity.User;
import com.example.spring_project.domain.repository.SessionRepository;
import com.example.spring_project.infrastructure.redis.methods.RedisMethods;
import java.util.List;
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
    String expires_in
  ) {
    String sessionID = UUID.randomUUID().toString();
    String[] userInfoRedis = new String[6];
    userInfoRedis[0] = user.getUlid();
    userInfoRedis[1] = user.getEmail();
    userInfoRedis[2] = user.getName();
    userInfoRedis[3] = access_token;
    userInfoRedis[4] = refresh_token;
    userInfoRedis[5] = expires_in;
    String registerResult = redisMethods.RegisterStrings(
      sessionID,
      userInfoRedis
    );
    if (registerResult == sessionID) {
      return sessionID;
    } else {
      throw new Error("Session Error on Redis!");
    }
  }

  @Override
  public List<?> CheckSession(String SessionID) {
    return redisMethods.GetStrings(SessionID);
  }

  @Override
  public String RefreshAccessToken(String sessionID, String[] newData) {
    return redisMethods.OverwriteData(sessionID, newData);
  }
}
