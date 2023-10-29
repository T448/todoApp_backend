package com.example.spring_project.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
                                item.getStartDate(),
                                item.getEndDate(),
                                item.getStartDateTime(),
                                item.getEndDateTime(),
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
            String startDateStr,
            String endDateStr,
            String startDateTimeStr,
            String endDateTimeStr,
            String timeZone, String accessToken, String email) {
        String newEventId = googleCalendarEventRepository.addNewEvent(name, memo, startDateStr, endDateStr,
                startDateTimeStr, endDateTimeStr,
                timeZone, projectId, accessToken);

        if (!newEventId.startsWith("[error]", 0)) {
            String shortTitle = name;
            if (shortTitle.length() > 10) {
                shortTitle = shortTitle.substring(0, 10) + "...";
            }

            Date startDateTime = null;
            if (!startDateTimeStr.isBlank()) {
                SimpleDateFormat sdfStartDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                try {
                    startDateTime = sdfStartDateTime.parse(startDateTimeStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            Date endDateTime = null;
            if (!endDateTimeStr.isBlank()) {
                SimpleDateFormat sdfEndDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                try {
                    endDateTime = sdfEndDateTime.parse(endDateTimeStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Date startDate = null;
            if (!startDateStr.isBlank()) {
                SimpleDateFormat sdfStartDate = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    startDate = sdfStartDate.parse(startDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Date endDate = null;
            if (!endDateStr.isBlank()) {
                SimpleDateFormat sdfEndDate = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    endDate = sdfEndDate.parse(endDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            eventRepository.RegisterEvents(
                    List.of(
                            new Event(newEventId, email, name, shortTitle, projectId, parentEventId, memo, startDate,
                                    endDate, startDateTime, endDateTime, null, null)));
        }
        return newEventId;
    }

    /**
     * 
     * @param eventId
     * @param name
     * @param memo
     * @param projectId
     * @param parentEventId
     * @param startDateTimeStr
     * @param endDateTime
     * @param timeZone
     * @param accessToken
     * @param email
     * @return
     */
    public String updateEvent(
            String eventId,
            String name,
            String memo,
            String projectId,
            String startDateStr,
            String endDateStr,
            String startDateTimeStr,
            String endDateTimeStr,
            String timeZone,
            String accessToken,
            String email) {
        log.info("[EventUsecase] update event");

        String updateResponseFromGoogleCalendar = googleCalendarEventRepository.updateEvent(
                eventId, name, memo, startDateStr, endDateStr, startDateTimeStr, endDateTimeStr, timeZone, projectId,
                accessToken);

        if (updateResponseFromGoogleCalendar.equals(eventId)) {
            Date startDateTime = null;
            if (!startDateTimeStr.isBlank()) {
                SimpleDateFormat sdfStartDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                try {
                    startDateTime = sdfStartDateTime.parse(startDateTimeStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Date endDateTime = null;
            if (!endDateTimeStr.isBlank()) {
                SimpleDateFormat sdfEndDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                try {
                    endDateTime = sdfEndDateTime.parse(endDateTimeStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Date startDate = null;
            if (!startDateStr.isBlank()) {
                SimpleDateFormat sdfStartDate = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    startDate = sdfStartDate.parse(startDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Date endDate = null;
            if (!endDateStr.isBlank()) {
                SimpleDateFormat sdfEndDate = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    endDate = sdfEndDate.parse(endDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Event updatedEvent = new Event(
                    eventId, email, name, name, projectId, null, memo, startDate, endDate, startDateTime, endDateTime,
                    null, null);
            eventRepository.UpdateEvent(updatedEvent);
        }
        return updateResponseFromGoogleCalendar;
    }

    public String deleteEvents(List<String> eventIdList, String email, String projectId, String accessToken) {
        List<String> deletedEventIdListOnGC = new ArrayList<String>();
        eventIdList.forEach(eventId -> {
            String result = googleCalendarEventRepository.deleteEvents(projectId, eventId, accessToken);
            if (result.isBlank()) {
                deletedEventIdListOnGC.add(eventId);
            }
        });
        eventRepository.deleteEvents(deletedEventIdListOnGC);
        return deletedEventIdListOnGC.toString();
    }
}
