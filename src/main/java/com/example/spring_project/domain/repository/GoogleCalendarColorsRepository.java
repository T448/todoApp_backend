package com.example.spring_project.domain.repository;

import java.util.List;

import com.example.spring_project.domain.entity.Color;

public interface GoogleCalendarColorsRepository {
    public List<Color> getGoogleCalendarColors(String email, String accessToken);
}
