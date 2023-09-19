package com.example.spring_project.infrastructure.googleApi;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.spring_project.domain.entity.Event;
import com.example.spring_project.domain.repository.GoogleCalendarRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class GoogleCalendarRepositoryImpl implements GoogleCalendarRepository{
    
    @Override
    public ArrayList<Event> GetGoogleCalendarEvents(String email,String accessToken,Date updatedMin){
        ArrayList<Event> events = new ArrayList<Event>();
        String requestUrl = "https://www.googleapis.com/calendar/v3/calendars/";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        try {
            requestUrl += URLEncoder.encode(email, "UTF-8") + "/events";
            requestUrl += "?updatedMin=" + sf.format(updatedMin);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
            .newBuilder()
            .uri(URI.create(requestUrl))
            .header("Authorization", "Bearer " + accessToken)
            .GET()
            .build();

        SimpleDateFormat start_dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat end_dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat created_at_dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat updated_at_dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        try{
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.body()).get("items");
            
            Integer loopCount = node.size();
            
            for (int i=0;i<loopCount;i++){
                JsonNode event = node.get(i);
               
                String id = event.get("id").toString();
                String created_at = event.get("created").toString();
                String updated_at = event.get("updated").toString();
                String summary = event.get("summary").toString();
                String start;
                String end;

                try{
                    start = event.get("start").get("dateTime").toString();
                } catch(Exception e) {
                    start = event.get("start").get("date").toString();
                    start += "T00:00:00";
                }

                try{
                    end = event.get("end").get("dateTime").toString();
                } catch(Exception e) {
                    end = event.get("end").get("date").toString().replaceAll("\"", "");
                    end += "T23:59:59";
                }
                id = id.replaceAll("\"","");
                email = email.replaceAll("\"","");
                summary = summary.replaceAll("\"","");
                start = start.replaceAll("\"", "");
                start = start.replaceAll("T", " ");
                start = start.replaceAll("\\+09:00", "");

                end = end.replaceAll("\"", "");
                end = end.replaceAll("T", " ");
                end = end.replaceAll("\\+09:00", "");

                created_at = created_at.replaceAll("\"", "");
                created_at = created_at.replaceAll("T", " ");
                created_at = created_at.replaceAll("Z", "");

                updated_at = updated_at.replaceAll("\"", "");
                updated_at = updated_at.replaceAll("T", " ");
                updated_at = updated_at.replaceAll("Z", "");

                Date start_Date = start_dateFormat.parse(start);
                Date end_Date = end_dateFormat.parse(end);
                Date created_at_Date = created_at_dateFormat.parse(created_at);
                Date updated_at_Date = updated_at_dateFormat.parse(updated_at);

                String parentEvent = "";
                
                Event eventObj = new Event(id, email, summary,parentEvent, start_Date,end_Date,created_at_Date,updated_at_Date);
                events.add(eventObj);
            }

            
        } catch(Exception e){
            e.printStackTrace();
        }
        return events;
    }
}
