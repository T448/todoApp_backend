package com.example.spring_project.domain.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Project {
    private String id;
    private String name;
    private String color;
    private String memo;
    private String email;
    private Date createdAt;
    private Date updatedAt;
}
