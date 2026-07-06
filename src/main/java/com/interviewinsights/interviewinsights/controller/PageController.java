package com.interviewinsights.interviewinsights.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    @GetMapping("/auth")
    public String auth() {
        return "forward:/auth.html";
    }

    @GetMapping("/index")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/companies")
    public String companies() {
        return "forward:/companies.html";
    }

    @GetMapping("/questions")
    public String questionsPage() {
        return "forward:/questions.html";
    }

    @GetMapping("/experiences")
    public String experiencesPage() {
        return "forward:/experiences.html";
    }

    @GetMapping("/submit-experience")
    public String submitExperiencePage() {
        return "forward:/submit-experience.html";
    }
}