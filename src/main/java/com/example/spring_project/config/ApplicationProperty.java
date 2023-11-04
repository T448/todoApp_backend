package com.example.spring_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/com/example/spring_project/config/application.properties")
@PropertySource("classpath:/com/example/spring_project/config/application-${spring.profiles.active}.properties")
public class ApplicationProperty {

  @Autowired
  private Environment env;

  public String get(String key) {
    return env.getProperty(key);
  }
}
