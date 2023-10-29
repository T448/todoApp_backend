package com.example.spring_project.infrastructure.googleApi;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Event;
import com.example.spring_project.domain.repository.GoogleCalendarRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GoogleCalendarRepositoryImpl implements GoogleCalendarRepository {

    @Override
    public ArrayList<Event> GetGoogleCalendarEvents(String email, String accessToken, Date updatedMin,
            String calendarId) {
        ArrayList<Event> events = new ArrayList<Event>();
        String requestUrl = "https://www.googleapis.com/calendar/v3/calendars/";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        log.info("GetGoogleCalendarEvents updatedMin");
        if (updatedMin != null) {
            log.info(updatedMin.toString());
        }
        try {
            requestUrl += URLEncoder.encode(calendarId, "UTF-8") + "/events";
            if (updatedMin != null) {
                requestUrl += "?updatedMin=" + sf.format(updatedMin);
            }

        } catch (UnsupportedEncodingException e) {
            // e.printStackTrace();
            log.error(e.toString());
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(requestUrl))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();
        log.info("GetGoogleCalendarEvents");
        log.info(request.toString());
        SimpleDateFormat start_dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat end_dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat created_at_dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat updated_at_dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.body()).get("items");

            Integer loopCount = node.size();

            for (int i = 0; i < loopCount; i++) {
                JsonNode event = node.get(i);
                if (event.has("summary")) {
                    log.info(event.toString());
                    String id = event.get("id").toString();
                    String created_at = event.get("created").toString();
                    String updated_at = event.get("updated").toString();
                    String title = event.get("summary").toString();
                    Date startDate = null;
                    Date endDate = null;
                    Date startDateTime = null;
                    Date endDateTime = null;
                    // Integer endDelta = 0;

                    try {
                        String startDateTimeStr = event.get("start").get("dateTime").toString().replaceAll("\"", "");
                        OffsetDateTime startOffsetDateTime = OffsetDateTime.parse(startDateTimeStr);
                        ZoneOffset offsetZoneStart = startOffsetDateTime.getOffset();
                        OffsetDateTime startUTCOffsetDateTime = startOffsetDateTime
                                .minusSeconds(offsetZoneStart.getTotalSeconds());
                        startDateTime = Date.from(startUTCOffsetDateTime.toInstant());
                        log.info("startDateTime");
                        log.info(startDateTime.toString());
                    } catch (Exception e) {
                        String startDateStr = event.get("start").get("date").toString().replaceAll("\"", "");
                        startDate = start_dateFormat.parse(startDateStr);
                        // start += "T00:00:00";
                    }

                    try {
                        String endDateTimeStr = event.get("end").get("dateTime").toString().replaceAll("\"", "");
                        OffsetDateTime endOffsetDateTime = OffsetDateTime.parse(endDateTimeStr);
                        ZoneOffset offsetZoneEnd = endOffsetDateTime.getOffset();
                        OffsetDateTime endUTCOffsetDateTime = endOffsetDateTime
                                .minusSeconds(offsetZoneEnd.getTotalSeconds());
                        endDateTime = Date.from(endUTCOffsetDateTime.toInstant());
                    } catch (Exception e) {
                        String endDateStr = event.get("end").get("date").toString().replaceAll("\"", "")
                                .replaceAll("\"", "");
                        endDate = end_dateFormat.parse(endDateStr);
                        // end += "T23:59:59";
                        // // 翌日の23:59:59になってしまうため、後で引く。
                        // endDelta = 1;
                    }
                    id = id.replaceAll("\"", "");
                    email = email.replaceAll("\"", "");
                    title = title.replaceAll("\"", "");
                    // if (startDate != null) {
                    // startDate = startDate.replaceAll("\"", "").replaceAll("T", " ");
                    // }
                    // start = start.replaceAll("\"", "");
                    // start = start.replaceAll("T", " ");
                    // start = start.replaceAll("\\+09:00", "");

                    // end = end.replaceAll("\"", "");
                    // end = end.replaceAll("T", " ");
                    // end = end.replaceAll("\\+09:00", "");

                    created_at = created_at.replaceAll("\"", "");
                    created_at = created_at.replaceAll("T", " ");
                    created_at = created_at.replaceAll("Z", "");

                    updated_at = updated_at.replaceAll("\"", "");
                    updated_at = updated_at.replaceAll("T", " ");
                    updated_at = updated_at.replaceAll("Z", "");

                    String shortTitle = title;
                    if (shortTitle.length() > 10) {
                        shortTitle = shortTitle.substring(0, 10) + "...";
                    }

                    // Date start_Date = start_dateFormat.parse(start);
                    // Date end_Date = end_dateFormat.parse(end);
                    // Calendar calendar = Calendar.getInstance();
                    // calendar.setTime(end_Date);
                    // calendar.add(Calendar.DATE, -endDelta);
                    // end_Date = calendar.getTime();
                    Date created_at_Date = created_at_dateFormat.parse(created_at);
                    Date updated_at_Date = updated_at_dateFormat.parse(updated_at);

                    String projectId = calendarId;
                    String parentEventId = "";
                    String memo = "";

                    Event eventObj = new Event(id, email, title, shortTitle, projectId, parentEventId, memo, startDate,
                            endDate, startDateTime, endDateTime, created_at_Date, updated_at_Date);
                    events.add(eventObj);
                }
            }

        } catch (Exception e) {
            log.error(e.toString());
        }
        return events;
    }
}
