package com.example.spring_project.common.methods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class TimeCalculator {

  public static String getTimeAfterSeconds(String additionalSeconds) {
    Calendar cdr = Calendar.getInstance();
    cdr.setTime(new Date());
    cdr.add(Calendar.SECOND, +Integer.parseInt(additionalSeconds));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String updatedTime = sdf.format(cdr.getTime());
    return updatedTime;
  }
}
