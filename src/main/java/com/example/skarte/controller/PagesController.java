package com.example.skarte.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.skarte.repository.UserRepository;
import com.example.skarte.entity.User;

@Controller
public class PagesController {

    @GetMapping("/")
    public String top() {
        return "pages/index";
    }

//    @GetMapping("/")
//    public String top(@PathVariable Long id, Model model) {
//        User user = userRepository.findByUserId(id);
//        model.addAttribute("users", user);
//        return "pages/index";
//    }

}
