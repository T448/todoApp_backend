package com.example.spring_project.presentation.model.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class DeleteEventsRequest {
    private String projectId;
    private List<String> eventIdList;

    public DeleteEventsRequest() {

    }
}
