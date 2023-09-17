package com.example.spring_project.presentation.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.spring_project.presentation.HideToken;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
public class HogeController {
      private final HideToken hideToken; 
    // 共通処理等の動作確認用のAPI
    @PostMapping(value = "/api/hoge")
    public String hoge(@RequestBody Map<String, Object> requestBody) {
        System.out.println("hoge");
        System.out.println(requestBody.get("auth"));
        System.out.println("----------------インターセプターで取得したトークン(hoge controller)------------------");
        System.out.println(hideToken.getHideTokenValue());
        return "hoge";
    }
}
