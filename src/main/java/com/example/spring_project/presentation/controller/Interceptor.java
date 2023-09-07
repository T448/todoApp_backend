package com.example.spring_project.presentation.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.spring_project.common.exceptions.ForbiddenException;
import com.example.spring_project.config.ApplicationProperty;
import com.example.spring_project.domain.entity.User;
import com.example.spring_project.domain.repository.SessionRepository;
import com.example.spring_project.infrastructure.googleApi.GoogleOauthRepositoryImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Interceptor implements HandlerInterceptor {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    private ApplicationProperty applicationProperty;
    @Autowired
    GoogleOauthRepositoryImpl googleOauthRepositoryImpl;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {

        Cookie cookie[] = request.getCookies();
        String sessionID = "";
        if (cookie != null) {
            for (int i = 0; i < cookie.length; i++) {
                if (cookie[i].getName().equals("sessionID")) {
                    sessionID = cookie[i].getValue();
                    break;
                }
            }
        }
        String requestURL = request.getRequestURL().toString();
        String loginURL = applicationProperty.get("spring.login_url");

        if (requestURL.equals(loginURL) == false) {
            List<?> checkSessionResult = sessionRepository.CheckSession(sessionID);
            if (checkSessionResult.size() == 0) {
                throw new ForbiddenException("session is not connected0");
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date expires = dateFormat.parse(checkSessionResult.get(5).toString());
                if (expires.before(new Date())) {
                    try {
                        var refreshResult = googleOauthRepositoryImpl.RefreshAccessToken(
                            new User(checkSessionResult.get(1).toString(),checkSessionResult.get(2).toString()),sessionID,checkSessionResult.get(4).toString());

                        String[] updatedSessionInfo = new String[6];
                        updatedSessionInfo[0] = checkSessionResult.get(0).toString();
                        updatedSessionInfo[1] = checkSessionResult.get(1).toString();
                        updatedSessionInfo[2] = checkSessionResult.get(2).toString();
                        updatedSessionInfo[3] = refreshResult.getAccessToken();
                        updatedSessionInfo[4] = checkSessionResult.get(4).toString();
                        updatedSessionInfo[5] = refreshResult.getExpires();

                        sessionRepository.RefreshAccessToken(sessionID, updatedSessionInfo);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }
}