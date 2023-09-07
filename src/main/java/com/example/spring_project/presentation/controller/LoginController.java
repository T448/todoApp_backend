package com.example.spring_project.presentation.controller;

import com.example.spring_project.service.LoginUsecase;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LoginController {

  @Autowired
  LoginUsecase loginUsecase;

  @PostMapping(value = "/api/login")
  public Map<String, Object> login(
    @RequestHeader(required = false) Map<String, Object> requestHeader
  ) {
    Map<String, Object> response = new HashMap();
    try {
      String sessionId = loginUsecase.login(
        requestHeader.get("authorization").toString()
      );
      response.put("sessionId", sessionId);
      return response;
    } catch (Exception error) {
      throw new IllegalArgumentException(error.toString());
    }
  }
}
