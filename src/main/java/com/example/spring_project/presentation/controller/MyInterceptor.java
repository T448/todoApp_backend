package com.example.spring_project.presentation.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.spring_project.domain.model.ForbiddenException;
import com.example.spring_project.usecase.SessionUsecase;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {
    @Autowired
    SessionUsecase sessionUsecase;

    String loginURL = "http://localhost:8080/api/login";

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
        Date date = new Date();
        System.out.println(date);
        System.out.println("session の確認を行います");
        if (requestURL.equals(loginURL) == false) {
            List<?> checkSessionResult = sessionUsecase.CheckSession(sessionID);
            System.out.println("checkSessionResult");
            System.out.println(checkSessionResult);
            if (checkSessionResult.size() == 0) {
                throw new ForbiddenException("session is not connected0");
            }
        }
        return true;
    }
}