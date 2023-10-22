package com.example.spring_project.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class MemoTemplate {
    private String id;
    private String name;
    private String user_email;
    private String template;
    private String created_at;
    private String updated_at;
}
