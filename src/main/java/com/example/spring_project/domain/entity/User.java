package com.example.spring_project.domain.entity;

import de.huxhorn.sulky.ulid.ULID;
import java.util.Date;
import lombok.Getter;

@Getter
public class User {

  private String ulid;
  private String email;
  private String name;
  private Date created_at;
  private Date updated_at;

  public User(String email, String name) {
    this.ulid = (new ULID()).toString();
    this.email = email;
    this.name = name;
  }
}
