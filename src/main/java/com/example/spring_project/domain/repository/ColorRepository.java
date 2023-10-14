package com.example.spring_project.domain.repository;

import java.util.List;

import com.example.spring_project.domain.entity.Color;

public interface ColorRepository {
    public List<Color> selectByEmail(String email);

    public void upsertColorList(List<Color> colorList);
}
