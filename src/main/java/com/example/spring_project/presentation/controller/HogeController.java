package com.example.spring_project.presentation.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.spring_project.presentation.HideValue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HogeController {
    private final HideValue hideValue;

    // 共通処理等の動作確認用のAPI
    @PostMapping(value = "/api/hoge")
    public String hoge(@RequestBody Map<String, Object> requestBody) {
        log.info("hoge");
        log.info(requestBody.get("auth").toString());
        log.info("----------------インターセプターで取得したトークン(hoge controller)------------------");
        log.info(hideValue.getHideTokenValue());
        return "hoge";
    }
}
