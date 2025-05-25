package com.example.skarte.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.skarte.entity.User;
import com.example.skarte.entity.User.Authority;
import com.example.skarte.form.UserForm;
import com.example.skarte.repository.UserRepository;

@Controller
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;

    // path: /users/new
    // 新規登録ページを表示
    @GetMapping("/new")
    public String newUser(Model model, @ModelAttribute UserForm form) {
        model.addAttribute("header", "ユーザー新規登録");
        model.addAttribute("form", new UserForm());
        return "users/new";
    }

    // path: /users/add
    // 画面で入力されたユーザー情報を取得して、dbに登録をする
    @PostMapping("/add")
    public String add(@ModelAttribute @Validated UserForm form, BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {
        String userId = form.getUserId();
        String lastName = form.getLastName();
        String firstName = form.getFirstName();
        String password = form.getPassword();

        if (repository.findByUserId(form.getUserId()) != null) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "そのユーザーIDはすでに使用されています");
            model.addAttribute("form", form);
            model.addAttribute("header", "ユーザー新規登録");
            return "users/new";
        }
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "登録に失敗しました");
            model.addAttribute("form", form);
            model.addAttribute("header", "ユーザー新規登録");
            return "users/new";
        }
        List<User> users = repository.findByOrderByUserIdAsc();
        // 最初に登録したユーザーを管理者にする
        if (users.isEmpty()) {
            User entity = new User(userId, lastName, firstName, passwordEncoder.encode(password), Authority.ROLE_ADMIN);
            entity.setCreatedBy(userId);
            entity.setUpdatedBy(userId);
            repository.saveAndFlush(entity);
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-info");
            model.addAttribute("message", "ユーザー登録が完了しました");
            model.addAttribute("header", "ログイン");
            return "sessions/login";
        } else {
            User entity = new User(userId, lastName, firstName, passwordEncoder.encode(password), Authority.ROLE_USER);
            entity.setCreatedBy(userId);
            entity.setUpdatedBy(userId);
            repository.saveAndFlush(entity);
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-info");
            model.addAttribute("message", "ユーザー登録が完了しました");
            model.addAttribute("header", "ログイン");
            return "sessions/login";
        }
    }
}