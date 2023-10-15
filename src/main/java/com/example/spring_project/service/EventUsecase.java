package com.example.spring_project.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Color;
import com.example.spring_project.domain.entity.Event;
import com.example.spring_project.domain.entity.Project;
import com.example.spring_project.domain.repository.ColorRepository;
import com.example.spring_project.domain.repository.EventRepository;
import com.example.spring_project.domain.repository.GoogleCalendarEventRepository;
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
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    GoogleCalendarEventRepository googleCalendarEventRepository;

    public List<EventDto> getEvents(String accessToken, String email, Boolean all) {
        Date latestUpdatedDate = eventRepository.GetLatestUpdatedDate(email);
        System.out.println("---------------最終更新---------------");
        System.out.println(latestUpdatedDate);
        try {
            List<Event> eventList = googleCalendarRepository.GetGoogleCalendarEvents(email, accessToken,
                    latestUpdatedDate);
            System.out.println("---------------eventList---------------");
            // System.out.println(eventList);
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
            List<Color> colorList = colorRepository.selectByEmail(email);
            Map<String, String> colorIdAndCodeMap = new HashMap<String, String>();
            colorList.forEach(item -> {
                colorIdAndCodeMap.put(item.getId(), item.getCode());
            });
            Map<String, Project> projectIdAndDetailMap = new HashMap<String, Project>();

            projectList.forEach(item -> projectIdAndDetailMap.put(item.getId(), item));
            List<EventDto> getEventDtoListResult = getEventListResult
                    .stream()
                    .map(item -> {
                        String thisProjectId = item.getProject_id();
                        Project thisProjectDetail = projectIdAndDetailMap.get(thisProjectId);
                        String colorCode = "#000000";
                        if (colorIdAndCodeMap.containsKey(thisProjectDetail.getColor_id())) {
                            colorCode = colorIdAndCodeMap.get(thisProjectDetail.getColor_id());
                        }
                        return new EventDto(
                                item.getId(),
                                item.getEmail(),
                                item.getTitle(),
                                item.getShort_title(),
                                item.getProject_id(),
                                thisProjectDetail.getName(),
                                colorCode,
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

    public String addEvents(String name,
            String memo,
            String projectId,
            String parentEventId,
            String startDateTime,
            String endDateTime,
            String timeZone, String accessToken, String email) {
        String newEventId = googleCalendarEventRepository.addNewEvent(name, memo, startDateTime, endDateTime,
                timeZone, projectId, accessToken);

        if (!newEventId.startsWith("[error]", 0)) {
            String shortTitle = name;
            if (shortTitle.length() > 10) {
                shortTitle = shortTitle.substring(0, 10) + "...";
            }
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            Date start;
            try {
                start = sdFormat.parse(startDateTime);
            } catch (ParseException e) {
                start = new Date();
            }
            Date end;
            try {
                end = sdFormat.parse(endDateTime);
            } catch (ParseException e) {
                end = new Date();
            }
            eventRepository.RegisterEvents(
                    List.of(
                            new Event(newEventId, email, name, shortTitle, projectId, parentEventId, memo, start, end,
                                    null, null)));
        }
        return newEventId;
    }
}
