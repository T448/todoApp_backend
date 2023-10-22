package com.example.spring_project.infrastructure.rdb.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.MemoTemplate;
import com.example.spring_project.domain.repository.MemoTemplateRepository;
import com.example.spring_project.infrastructure.rdb.mapper.MemoTemplateMapper;

@Service
public class MemoTemplateRepositoryDB implements MemoTemplateRepository {

    @Autowired
    MemoTemplateMapper memoTemplateMapper;

    @Override
    public List<MemoTemplate> selectByEmail(String email) {
        return memoTemplateMapper.selectByEmail(email);
    }

    @Override
    public void insert(String id, String name, String template, String user_email) {
        memoTemplateMapper.insert(id, name, template, user_email);
    }

    @Override
    public void update(String id, String name, String template) {
        memoTemplateMapper.update(id, name, template);
    }
}
