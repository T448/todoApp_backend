package com.example.spring_project.presentation.controller.Response;

import java.util.Date;
import java.util.Map;

import com.example.spring_project.domain.entity.Project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GetProjectResponse {
    private String id;
    private String name;
    // color code
    private String color;
    private String memo;
    private String email;
    private Date createdAt;
    private Date updatedAt;

    public GetProjectResponse(
            Project project,
            Map<String, String> colorIdAndCodeMap) {
        this.id = project.getId();
        this.name = project.getName();
        if (colorIdAndCodeMap.containsKey(project.getColor_id())) {
            this.color = colorIdAndCodeMap.get(project.getColor_id());
        } else {
            this.color = "#000000";
        }
        this.memo = project.getMemo();
        this.email = project.getEmail();
        this.createdAt = project.getCreatedAt();
        this.updatedAt = project.getUpdatedAt();
    }
}
