package com.example.spring_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.MemoTemplate;
import com.example.spring_project.domain.repository.MemoTemplateRepository;

import de.huxhorn.sulky.ulid.ULID;

@Service
public class MemoTemplateUsecase {

    @Autowired
    MemoTemplateRepository memoTemplateRepository;

    public List<MemoTemplate> selectByEmail(String email) {
        var res = memoTemplateRepository.selectByEmail(email);
        if (res == null) {
            return List.of();
        }
        return res;
    }

    public void insert(String id, String name, String template, String user_email) {
        if (id.equals("add")) {
            ULID newTemplateId = new ULID();
            memoTemplateRepository.insert(newTemplateId.nextULID(), name, template, user_email);
        }
    }

    public void update(String id, String name, String template) {
        memoTemplateRepository.update(id, name, template);
    }

}
