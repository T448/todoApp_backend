package com.example.spring_project.presentation.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class HogeController {
    // 共通処理等の動作確認用のAPI
    @PostMapping(value = "/api/hoge")
    public String hoge(String inputValue) {
        System.out.println("hoge");
        return "hoge";
    }
}
