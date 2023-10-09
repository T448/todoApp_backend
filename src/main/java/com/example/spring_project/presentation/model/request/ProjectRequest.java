package com.example.spring_project.presentation.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ProjectRequest {
    private String name;
    private String color;
    private String memo;

    public ProjectRequest() {

    }
}
