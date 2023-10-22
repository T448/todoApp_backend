package com.example.spring_project.domain.repository;

import java.util.List;

import com.example.spring_project.domain.entity.MemoTemplate;

public interface MemoTemplateRepository {
    public List<MemoTemplate> selectByEmail(String user_email);

    public void insert(String id, String name, String template, String user_email);

    public void update(String id, String name, String template);
}
