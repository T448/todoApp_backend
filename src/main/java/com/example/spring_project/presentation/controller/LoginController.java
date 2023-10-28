package com.example.spring_project.presentation.controller;

import com.example.spring_project.common.response.LoginResponse;
import com.example.spring_project.service.LoginUsecase;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

  @Autowired
  LoginUsecase loginUsecase;

  @PostMapping(value = "/api/login")
  public LoginResponse login(
      @RequestHeader(required = false) Map<String, Object> requestHeader) {
    try {
      log.info("-----[login controller]-----");
      String sessionId = loginUsecase.login(
          requestHeader.get("authorization").toString());
      LoginResponse response = new LoginResponse(sessionId);
      return response;
    } catch (Exception error) {
      log.error(error.toString());
      throw new IllegalArgumentException(error.toString());
    }
  }
}
