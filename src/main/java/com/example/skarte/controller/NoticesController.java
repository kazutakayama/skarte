package com.example.skarte.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.skarte.entity.Notice;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.User;
import com.example.skarte.form.NoticeForm;
import com.example.skarte.service.AttendanceService;
import com.example.skarte.service.GradeService;
import com.example.skarte.service.NoticesService;
import com.example.skarte.service.ScheduleService;
import com.example.skarte.service.StudentsService;
import com.example.skarte.service.StudentsYearService;
import com.example.skarte.service.UsersService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticesController {

    private final NoticesService noticesService;

    // path: /notices
    // お知らせ一覧ページを表示
    @GetMapping("")
    public String index(Model model) {
        List<Notice> notices = noticesService.findAll();
        model.addAttribute("notices", notices);
        return "notices/index";
    }

    // path: /notices/add
    // 画面で入力されたお知らせを取得して、dbに登録をする
    @PostMapping("/add")
    public String add(NoticeForm noticeForm, @AuthenticationPrincipal User user) {
        noticesService.add(user.getUserId(), noticeForm);
        return "redirect:/notices";
    }

    // path: /notices/contents/{noticeId}/edit
    // お知らせ詳細からお知らせ編集へのリンク
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Notice notice = noticesService.findById(id);
        model.addAttribute("notice", notice);
        return "notices/edit";
    }

    // path: /notices/contents/{id}/update
    // 編集画面からお知らせを更新する
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute NoticeForm noticeForm,
            @AuthenticationPrincipal User user) {
        noticesService.update(id, noticeForm, user.getUserId());
        return "redirect:/notices";
    }

    // path: /notices/contents/{id}/delete
    // お知らせ詳細からお知らせを削除（物理削除）
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @ModelAttribute Notice notice) {
        noticesService.delete(id);
        return "redirect:/notices";
    }
}