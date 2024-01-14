package com.example.skarte.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticesController {
    
    @GetMapping("/notices")
    public String top() {
        // NoticesService.javaから画面表示に必要なデータを取得する
        return "notices/index";
    }
}
