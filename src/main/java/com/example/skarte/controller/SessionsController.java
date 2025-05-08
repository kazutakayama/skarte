package com.example.skarte.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class SessionsController {

    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @GetMapping("/login")
    public String index(Model model) {
        model.addAttribute("header", "ログイン");
        return "sessions/login";
    }

    @GetMapping("/login-failure")
    public String loginFailure(Model model) {
        model.addAttribute("header", "ログイン");
        model.addAttribute("hasMessage", true);
        model.addAttribute("class", "alert-danger");
        model.addAttribute("message", "ユーザーIDまたはパスワードに誤りがあります");
        return "sessions/login";
    }

    @GetMapping("/logout-complete")
    public String logoutComplete(Model model, Authentication authentication, HttpServletRequest request,
            HttpServletResponse response, RedirectAttributes redirectAttributes) {
        this.logoutHandler.logout(request, response, authentication);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "ログアウトしました");
        return "redirect:/login";
    }
}