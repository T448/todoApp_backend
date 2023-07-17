package com.example.spring_project.domain.model;

import java.util.Date;
import lombok.Data;

@Data
public class User {
    private String ulid;
    private String email;
    private String name;
    private Date created_at;
    private Date updated_at;
}
