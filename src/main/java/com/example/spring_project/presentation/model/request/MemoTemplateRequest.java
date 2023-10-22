package com.example.spring_project.presentation.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class MemoTemplateRequest {
    private String id;
    private String name;
    private String template;

    public MemoTemplateRequest() {

    }
}
