package com.example.spring_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import redis.clients.jedis.Jedis;

import com.example.spring_project.domain.model.User;
import com.example.spring_project.domain.service.UserService;

import de.huxhorn.sulky.ulid.ULID;

@RestController
public class LoginController {
    @Autowired
    UserService service;

    @PostMapping(value = "/login", consumes = "application/json")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, Object> login(@RequestBody(required = false) Map<String, Object> request) {

        Map<String, Object> response = new HashMap();

        if (request == null) {
            response.put("error", "リクエストボディが不正です。");
        } else {
            String email = request.get("email").toString();
            String name = request.get("name").toString();
            if (email.contains("@gmail.com") == false) {
                response.put("error", "gmailではありません。");
            } else {
                // session生成部 別ファイルにする。
                String sessionID = UUID.randomUUID().toString();
                Jedis jedis = new Jedis("redis", 6379);
                String[] userInfoRedis = new String[2];
                userInfoRedis[0] = email;
                userInfoRedis[1] = name;
                jedis.rpush(sessionID, userInfoRedis);
                jedis.close();
                // session生成部 ここまで。
                response.put("sessionID", sessionID);

                User userInfo = service.selectOneUser(email);
                if (userInfo == null) {
                    ULID user_ulid = new ULID();
                    String user_id = user_ulid.nextULID();
                    User newUser = new User();
                    newUser.setUlid(user_id);
                    newUser.setEmail(email);
                    newUser.setName(name);
                    service.insert(newUser);
                    // 新規登録後に取得する。created_at等を受け取るため。
                    userInfo = service.selectOneUser(email);
                }
                response.put("user_info", userInfo);
            }
        }
        return response;
    }
}