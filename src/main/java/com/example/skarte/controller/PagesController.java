package com.example.skarte.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.skarte.repository.UserRepository;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.User;




@Controller
public class PagesController {
    
    @GetMapping("/")
    public String index() {
//        return "pages/index";
        return "redirect:/top";
    }

    @GetMapping("/top")
    public String top() {
        return "pages/index";
    }


}
