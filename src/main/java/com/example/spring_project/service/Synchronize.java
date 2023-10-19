package com.example.spring_project.service;

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
import com.example.spring_project.domain.repository.GoogleCalendarCalendarRepository;
import com.example.spring_project.domain.repository.GoogleCalendarColorsRepository;
import com.example.spring_project.domain.repository.GoogleCalendarEventRepository;
import com.example.spring_project.domain.repository.GoogleCalendarRepository;
import com.example.spring_project.domain.repository.ProjectRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Synchronize {
    @Autowired
    ColorRepository colorRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    GoogleCalendarColorsRepository googleCalendarColorsRepository;

    @Autowired
    GoogleCalendarCalendarRepository googleCalendarCalendarRepository;

    @Autowired
    GoogleCalendarEventRepository googleCalendarEventRepository;

    @Autowired
    GoogleCalendarRepository googleCalendarRepository;

    /**
     * GCで追加等行ったものの変更内容をDBに反映するためのもの。
     * 
     * @param email
     * @param accessToken
     */
    public void synchronize(String email, String accessToken) {
        log.info("---------------Synchronize---------------");
        List<Color> colorListDB = colorRepository.selectByEmail(email);
        List<Project> projectListDB = projectRepository.selectByEmail(email);
        List<Event> eventListDB = eventRepository.selectByEmail(email);

        List<Color> colorListGC = googleCalendarColorsRepository.getGoogleCalendarColors(email, accessToken);
        List<Project> projectListGC = googleCalendarCalendarRepository.getCalendarList(accessToken, email);
        List<String> projectIdListGC = projectListGC.stream().map(item -> item.getId()).toList();

        ArrayList<List<Event>> eventListListGC = new ArrayList<>();
        projectIdListGC.forEach(item -> {
            eventListListGC.add(googleCalendarRepository.GetGoogleCalendarEvents(email,
                    accessToken, null, item));
        });
        log.info("eventListListGC");
        log.info(eventListListGC.toString());
        List<Event> eventListGC = eventListListGC.stream().flatMap(list -> list.stream()).toList();

        var divideColorsResultMap = divideColors(colorListDB, colorListGC, email);
        var divideProjectsResultMap = divideProjects(projectListDB, projectListGC, email);
        var divideEventsResultMap = divideEvents(eventListDB, eventListGC, email);
        // color
        log.info(divideColorsResultMap.toString());
        var newColorListDB = divideColorsResultMap.get("newColorListDB");
        var updateColorListDB = divideColorsResultMap.get("updateColorListDB");
        var deleteColorListDB = divideColorsResultMap.get("deleteColorListDB");

        if (!newColorListDB.isEmpty()) {
            colorRepository.upsertColorList(newColorListDB);
        }
        if (!updateColorListDB.isEmpty()) {
            colorRepository.upsertColorList(updateColorListDB);
        }
        if (!deleteColorListDB.isEmpty()) {
            colorRepository.deleteColorList(deleteColorListDB);
        }

        // project
        log.info(divideProjectsResultMap.toString());
        var newProjectListDB = divideProjectsResultMap.get("newProjectListDB");
        var updateProjectListDB = divideProjectsResultMap.get("updateProjectListDB");
        var deleteProjectListDB = divideProjectsResultMap.get("deleteProjectListDB");

        if (!newProjectListDB.isEmpty()) {
            projectRepository.insertProjects(newProjectListDB);
        }

        if (!updateProjectListDB.isEmpty()) {
            projectRepository.updateProjects(updateProjectListDB);
        }

        if (!deleteProjectListDB.isEmpty()) {
            projectRepository.deleteProjects(deleteProjectListDB);
        }

        // event
        log.info(divideEventsResultMap.toString());
        var newEventListDB = divideEventsResultMap.get("newEventListDB");
        var updateEventListDB = divideEventsResultMap.get("updateEventListDB");
        var deleteEventListDB = divideEventsResultMap.get("deleteEventListDB");

        if (!newEventListDB.isEmpty()) {
            eventRepository.RegisterEvents(newEventListDB);
        }

        if (!updateEventListDB.isEmpty()) {
            eventRepository.UpdateEvents(updateEventListDB);
        }

        if (!deleteEventListDB.isEmpty()) {
            eventRepository.deleteEvents(deleteEventListDB);
        }

    }

    private Map<String, List<Color>> divideColors(List<Color> colorListDB, List<Color> colorListGC, String email) {
        Map<String, String> colorMapDB = new HashMap<>();
        Map<String, String> colorMapGC = new HashMap<>();
        colorListDB.forEach(item -> colorMapDB.put(item.getId(), item.getCode()));
        colorListGC.forEach(item -> colorMapGC.put(item.getId(), item.getCode()));

        ArrayList<Color> newColorList = new ArrayList<Color>();
        ArrayList<Color> updateColorList = new ArrayList<Color>();
        ArrayList<Color> deleteColorList = new ArrayList<Color>();
        colorListDB.forEach(item -> {
            String colorIdDB = item.getId();
            String colorCodeDB = item.getCode();
            if (colorMapGC.containsKey(colorIdDB)) {
                // あるIDがDBにあってGCにもある→更新orなにもしない
                String colorCodeGC = colorMapGC.get(colorIdDB);
                if (!colorCodeDB.equals(colorCodeGC)) {
                    // 同じ色ではない→更新
                    updateColorList.add(new Color(colorIdDB, colorCodeGC, email));
                }
            } else {
                // あるIDがDBにあってGCにない→削除
                deleteColorList.add(new Color(colorIdDB, colorCodeDB, email));
            }
        });
        colorListGC.forEach(item -> {
            String colorIdGC = item.getId();
            String colorCodeGC = item.getCode();
            if (!colorMapDB.containsKey(colorIdGC)) {
                // あるIDがDBになくてGCにある→追加
                newColorList.add(new Color(colorIdGC, colorCodeGC, email));
            }
        });

        Map<String, List<Color>> returnMap = new HashMap<>();
        returnMap.put("newColorListDB", newColorList);
        returnMap.put("updateColorListDB", updateColorList);
        returnMap.put("deleteColorListDB", deleteColorList);

        return returnMap;
    }

    private Map<String, List<Project>> divideProjects(List<Project> projectListDB, List<Project> projectListGC,
            String email) {
        Map<String, Project> projectMapDB = new HashMap<>();
        Map<String, Project> projectMapGC = new HashMap<>();

        projectListDB.forEach(item -> projectMapDB.put(item.getId(), item));
        projectListGC.forEach(item -> projectMapGC.put(item.getId(), item));

        ArrayList<Project> newProjectListForDB = new ArrayList<Project>();
        ArrayList<Project> updateProjectListForDB = new ArrayList<Project>();
        ArrayList<Project> deleteProjectListForDB = new ArrayList<Project>();

        projectListDB.forEach(item -> {
            String projectIdDB = item.getId();
            if (projectMapGC.containsKey(projectIdDB)) {
                // あるIDがDBにあってGCにもある→追加or何もしない
                Project projectGC = projectMapGC.get(projectIdDB);
                String projectNameGC = projectGC.getName();
                String projectMemoGC = "";
                if (projectGC.getMemo() != null) {
                    projectMemoGC = projectGC.getMemo();
                }
                String projectColorIdGC = projectGC.getColor_id();

                String projectNameDB = item.getName();
                String projectMemoDB = item.getMemo();
                String projectColorIdDB = item.getColor_id();
                if (!projectNameDB.equals(projectNameGC) || !projectMemoDB.equals(projectMemoGC)
                        || !projectColorIdDB.equals(projectColorIdGC)) {
                    // name,memo,colorIDのいずれかが不一致→更新
                    updateProjectListForDB.add(projectGC);
                }
            } else {
                // あるIDがDBにあってGCにない→削除
                deleteProjectListForDB.add(item);
            }
        });

        projectListGC.forEach(item -> {
            String projectIdGC = item.getId();
            if (!projectMapDB.containsKey(projectIdGC)) {
                // あるIDがDBになくてGCにある→追加
                newProjectListForDB.add(item);
            }
        });

        Map<String, List<Project>> returnMap = new HashMap<>();
        returnMap.put("newProjectListDB", newProjectListForDB);
        returnMap.put("updateProjectListDB", updateProjectListForDB);
        returnMap.put("deleteProjectListDB", deleteProjectListForDB);

        return returnMap;
    }

    private Map<String, List<Event>> divideEvents(List<Event> eventListDB, List<Event> eventListGC, String email) {
        Map<String, Event> eventMapDB = new HashMap<>();
        Map<String, Event> eventMapGC = new HashMap<>();

        eventListDB.forEach(item -> eventMapDB.put(item.getId(), item));
        eventListGC.forEach(item -> eventMapGC.put(item.getId(), item));

        ArrayList<Event> newEventListForDB = new ArrayList<Event>();
        ArrayList<Event> updateEventListForDB = new ArrayList<Event>();
        ArrayList<Event> deleteEventListForDB = new ArrayList<Event>();

        eventListDB.forEach(item -> {
            String eventIdDB = item.getId();
            if (eventMapGC.containsKey(eventIdDB)) {
                // あるIDがDBにあってGCにもある→追加or何もしない
                Event eventGC = eventMapGC.get(eventIdDB);
                String eventTitleGC = eventGC.getTitle();
                String eventMemoGC = "";
                if (eventGC.getMemo() != null) {
                    eventMemoGC = eventGC.getMemo();
                }
                String eventProjectIdGC = eventGC.getProject_id();
                Date eventStartGC = eventGC.getStart();
                Date eventEndGC = eventGC.getEnd();
                if (!item.getTitle().equals(eventTitleGC) || eventMemoGC.equals(eventMemoGC)
                        || !item.getProject_id().equals(eventProjectIdGC) || !item.getStart().equals(eventStartGC)
                        || !item.getEnd().equals(eventEndGC)) {
                    updateEventListForDB.add(eventGC);
                }
            } else {
                deleteEventListForDB.add(item);
            }
        });
        eventListGC.forEach(item -> {
            String eventIdGC = item.getId();
            if (!eventMapDB.containsKey(eventIdGC)) {
                newEventListForDB.add(item);
            }
        });

        Map<String, List<Event>> returnMap = new HashMap<>();
        returnMap.put("newEventListDB", newEventListForDB);
        returnMap.put("updateEventListDB", updateEventListForDB);
        returnMap.put("deleteEventListDB", deleteEventListForDB);

        return returnMap;
    }
}
