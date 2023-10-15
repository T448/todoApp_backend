package com.example.spring_project.infrastructure.rdb.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.spring_project.domain.entity.Event;

@Mapper
public interface EventMapper {
    public boolean registerEvents(@Param("eventList") List<Event> eventList);

    public List<Event> getEvents(@Param("user_email") String email, @Param("updated_at") Date updated_at);

    public Date getLatestUpdatedDate(String email);

    public void updateEvent(@Param("event") Event event);

    public void updateEvents(@Param("event_list") List<Event> eventList);

}
