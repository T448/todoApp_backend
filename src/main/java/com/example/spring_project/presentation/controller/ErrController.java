package com.example.spring_project.presentation.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrController implements ErrorController{
    
    @RequestMapping("/401")
    String home() {
        return "test/error";
    }
}
