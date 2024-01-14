package com.example.skarte.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GradesController {

    @GetMapping("/grades")
    public String top() {
        return "grades/index";
    }
}
