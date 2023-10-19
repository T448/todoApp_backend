package com.example.spring_project.infrastructure.rdb.repository;

import com.example.spring_project.domain.repository.ColorRepository;
import com.example.spring_project.infrastructure.rdb.mapper.ColorMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Color;

@Service
public class ColorRepositoryDB implements ColorRepository {
    @Autowired
    private ColorMapper colorMapper;

    @Override
    public List<Color> selectByEmail(String email) {
        return colorMapper.selectByEmail(email);
    }

    @Override
    public void upsertColorList(List<Color> colorList) {
        colorMapper.upsertColorList(colorList);
    }

    @Override
    public void deleteColorList(List<Color> colorList) {
        colorMapper.deleteColorList(colorList);
    }
}
