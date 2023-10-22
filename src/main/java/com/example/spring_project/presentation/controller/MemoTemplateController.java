package com.example.spring_project.presentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_project.domain.entity.MemoTemplate;
import com.example.spring_project.presentation.HideValue;
import com.example.spring_project.presentation.model.request.MemoTemplateRequest;
import com.example.spring_project.service.MemoTemplateUsecase;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
public class MemoTemplateController {

    @Autowired
    MemoTemplateUsecase memoTemplateUsecase;

    private final HideValue hideValue;

    @GetMapping(value = "/api/memo")
    public List<MemoTemplate> getMemoTemplates() {
        String email = hideValue.getHideEmailValue();
        return memoTemplateUsecase.selectByEmail(email);
    }

    @PostMapping(value = "/api/memo")
    public void addMemoTemplate(@RequestBody MemoTemplateRequest request) {
        log.info(request.toString());
        String email = hideValue.getHideEmailValue();
        memoTemplateUsecase.insert(request.getId(), request.getName(), request.getTemplate(), email);
    }

    @PutMapping(value = "/api/memo")
    public void updateMemoTemplate(@RequestBody MemoTemplateRequest request) {
        log.info(request.toString());
        memoTemplateUsecase.update(request.getId(), request.getName(), request.getTemplate());
    }
}
