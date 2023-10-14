package com.example.spring_project.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Event;
import com.example.spring_project.domain.entity.Project;
import com.example.spring_project.domain.repository.EventRepository;
import com.example.spring_project.domain.repository.GoogleCalendarRepository;
import com.example.spring_project.domain.repository.ProjectRepository;

@Service
public class EventUsecase {

    @Autowired
    GoogleCalendarRepository googleCalendarRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    ProjectRepository projectRepository;

    public List<EventDto> getEvents(String accessToken, String email, Boolean all) {
        Date latestUpdatedDate = eventRepository.GetLatestUpdatedDate(email);
        System.out.println("---------------最終更新---------------");
        System.out.println(latestUpdatedDate);
        try {
            List<Event> eventList = googleCalendarRepository.GetGoogleCalendarEvents(email, accessToken,
                    latestUpdatedDate);
            System.out.println("---------------eventList---------------");
            System.out.println(eventList);
            Number registerResult = -1;
            if (!eventList.isEmpty()) {
                Project generalProject = projectRepository.selectByNameAndEmail("General", email);
                String generalProjectId = generalProject.getId();
                eventList.stream()
                        .forEach(item -> item.setProjectId(generalProjectId));
                registerResult = eventRepository.RegisterEvents(eventList);
            }
            System.out.println("---------------DBに新たに登録された件数---------------");
            System.out.println(registerResult);
            if (all) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                latestUpdatedDate = sdf.parse("1970-01-01 00:00:00");
            }
            // String latestUpdatedDateStr = "1970-01-01 00:00:00";
            System.out.println("---------------SELECT対象---------------");
            System.out.println(latestUpdatedDate);
            List<Event> getEventListResult = eventRepository.GetEvents(email, latestUpdatedDate);
            System.out.println(getEventListResult);
            List<Project> projectList = projectRepository.selectByEmail(email);
            Map<String, Project> projectIdAndDetailMap = new HashMap<String, Project>();

            projectList.forEach(item -> projectIdAndDetailMap.put(item.getId(), item));
            List<EventDto> getEventDtoListResult = getEventListResult
                    .stream()
                    .map(item -> {
                        String thisProjectId = item.getProject_id();
                        Project thisProjectDetail = projectIdAndDetailMap.get(thisProjectId);
                        return new EventDto(
                                item.getId(),
                                item.getEmail(),
                                item.getTitle(),
                                item.getShort_title(),
                                item.getProject_id(),
                                thisProjectDetail.getName(),
                                thisProjectDetail.getColor_id(),
                                item.getParent_event_id(),
                                List.of(),
                                item.getMemo(),
                                item.getStart(),
                                item.getEnd(),
                                item.getCreated_at(),
                                item.getUpdated_at());
                    })
                    .toList();
            getEventDtoListResult.forEach(item -> item.setChildEventIdList(getEventListResult));
            return getEventDtoListResult;
        } catch (Exception e) {
            throw new Error(e.toString());
        }
    }
}
