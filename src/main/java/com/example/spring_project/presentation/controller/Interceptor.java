package com.example.spring_project.presentation.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.spring_project.config.ApplicationProperty;
import com.example.spring_project.domain.entity.User;
import com.example.spring_project.domain.repository.SessionRepository;
import com.example.spring_project.infrastructure.googleApi.GoogleOauthRepositoryImpl;
import com.example.spring_project.infrastructure.redis.model.RedisUserInfo;
import com.example.spring_project.presentation.HideValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

// import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Interceptor implements HandlerInterceptor {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    private ApplicationProperty applicationProperty;
    @Autowired
    GoogleOauthRepositoryImpl googleOauthRepositoryImpl;

    private final HideValue hideValue;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String sessionID = request.getHeader("sessionID");
        String requestURL = request.getRequestURL().toString();
        String loginURL = applicationProperty.get("spring.login_url");
        System.out.println("Interceptor");
        System.out.println(sessionID);
        System.out.println(requestURL);
        System.out.println(loginURL);
        if (requestURL.equals("http://localhost:8080/api/redis-flush")) {
            // redisのデータを全消去したいとき用
            // 以降のセッション系の処理とは無関係のためそのままreturn
            return true;
        }
        if (requestURL.equals(loginURL)) {
            // ログイン処理実行時にセッションを発行するので以降の処理は不要
            // そのままreturn
            return true;
        }
        String checkSessionResultJsonString = sessionRepository.CheckSession(sessionID);
        System.out.println("checkSessionResultJsonString");
        System.out.println(checkSessionResultJsonString);
        if (checkSessionResultJsonString == null) {
            response.sendError(401, "sessionIDが不正です。");
            return false;

        }
        ObjectMapper objectMapperForRead = new ObjectMapper();
        RedisUserInfo checkSessionResult = objectMapperForRead.readValue(checkSessionResultJsonString,
                RedisUserInfo.class);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date expires = dateFormat.parse(checkSessionResult.getExpires());
        if (expires.after(new Date())) {
            hideValue.setHideTokenValue(checkSessionResult.getAccessToken());
            hideValue.setHideEmailValue(checkSessionResult.getEmail());
            return true;
        } else {
            System.out.println("refresh access token");
            try {
                var refreshResult = googleOauthRepositoryImpl.RefreshAccessToken(
                        new User(checkSessionResult.getEmail(), checkSessionResult.getName()), sessionID,
                        checkSessionResult.getRefreshToken());
                String updatedAccessToken = refreshResult.getAccessToken();
                hideValue.setHideTokenValue(updatedAccessToken);
                hideValue.setHideEmailValue(checkSessionResult.getEmail());
                RedisUserInfo updatedSessionInfo = new RedisUserInfo(checkSessionResult.getUlid(),
                        checkSessionResult.getEmail(), checkSessionResult.getName(), updatedAccessToken,
                        checkSessionResult.getRefreshToken(), refreshResult.getExpires());

                ObjectMapper objectMapperForWrite = new ObjectMapper();
                objectMapperForWrite.enable(SerializationFeature.INDENT_OUTPUT);
                String updatedSessionInfoJsonString = objectMapperForWrite.writeValueAsString(updatedSessionInfo);

                sessionRepository.RefreshAccessToken(sessionID, updatedSessionInfoJsonString);

            } catch (IOException e) {
                response.sendError(400, "refresh failed \n" + e.toString());
                return false;
            }
        }
        return true;
    }
}