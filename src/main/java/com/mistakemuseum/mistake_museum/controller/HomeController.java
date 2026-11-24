package com.mistakemuseum.mistake_museum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    // Home page: serves templates/index.html
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Upload page: serves templates/upload.html
    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    // Simple check that the app is alive
    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "OK";
    }
}