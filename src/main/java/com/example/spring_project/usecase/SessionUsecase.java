package com.example.spring_project.usecase;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.spring_project.domain.model.User;

@Service
public class SessionUsecase {
    @Autowired
    UserUsecase userUsecase;

    public String GenerateSession(String email, String name) {
        String sessionID = UUID.randomUUID().toString();
        Jedis jedis = new Jedis("redis", 6379);
        // 新規ユーザー登録(内部でif文により、未登録の場合のみ登録処理を行うようにしている。)
        User user_info = userUsecase.register(email, name);
        String[] userInfoRedis = new String[3];
        userInfoRedis[0] = user_info.getUlid();
        userInfoRedis[1] = email;
        userInfoRedis[2] = name;
        jedis.rpush(sessionID, userInfoRedis);
        jedis.close();
        return sessionID;
    }

    public List<?> CheckSession(String sessionID) {
        // Session切れだとしてもここではエラーを返さず、共通処理部分でエラー処理を行う。
        Jedis jedis = new Jedis("redis", 6379);
        List<?> checkSessionResult = jedis.lrange(sessionID, 0, -1);
        jedis.close();
        return checkSessionResult;
    }
}
