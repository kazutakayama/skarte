package com.example.skarte.controller;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.skarte.entity.Notice;
import com.example.skarte.entity.User;
import com.example.skarte.form.NoticeForm;
import com.example.skarte.service.NoticesService;
import com.example.skarte.service.UsersService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticesController {

    private final NoticesService noticesService;
    private final UsersService usersService;

    // path: /notices
    // お知らせ一覧ページを表示
    @GetMapping("")
    public String index(Model model) {
        List<Notice> notices = noticesService.convertUrlsToLinks();
        model.addAttribute("notices", notices);
        // Modelに"form"が存在しない時だけ、下記の処理を実行（リダイレクトされたBindingResultが失われないようにする）
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new NoticeForm());
        }
        // Modelに"notice"が存在しない時だけ、下記の処理を実行
        if (!model.containsAttribute("targetNotice")) {
            model.addAttribute("targetNotice", new Notice());
        }
        return "notices/index";
    }

    // path: /notices/add
    // お知らせ新規登録
    @PostMapping("/add")
    public String add(@ModelAttribute @Validated NoticeForm form, BindingResult result, Model model,
            @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("hasMessage", true);
            redirectAttributes.addFlashAttribute("class", "alert-danger");
            redirectAttributes.addFlashAttribute("message", "登録に失敗しました");
            redirectAttributes.addFlashAttribute("showNewModal", true); // newModal表示
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.form", result);
            redirectAttributes.addFlashAttribute("form", form);
            return "redirect:/notices";
        }
        noticesService.add(user.getUserId(), form);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "お知らせを登録しました");
        return "redirect:/notices";
    }

    // path: /notices/{noticeId}
    // お知らせ編集のModalを表示
    @GetMapping("/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Notice notice = noticesService.findById(id);
        redirectAttributes.addFlashAttribute("targetNotice", notice);
        redirectAttributes.addFlashAttribute("showEditModal", true); // editModal表示
        User teacher = usersService.findById(notice.getUpdatedBy());
        String userName;
        if (teacher != null) {
            userName = teacher.getLastName() + " " + teacher.getFirstName();
        } else {
            userName = notice.getUpdatedBy();
        }
        redirectAttributes.addFlashAttribute("userName", userName);
        return "redirect:/notices";
    }

    // path: /notices/{noticeId}/update
    // 編集画面からお知らせを更新する
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute @Validated NoticeForm form, BindingResult result,
            @AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("hasMessage", true);
            redirectAttributes.addFlashAttribute("class", "alert-danger");
            redirectAttributes.addFlashAttribute("message", "更新に失敗しました");
            redirectAttributes.addFlashAttribute("showEditModal", true); // editModal表示
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.form", result);
            redirectAttributes.addFlashAttribute("form", form);
            Notice notice = noticesService.findById(id);
            redirectAttributes.addFlashAttribute("targetNotice", notice);
            User teacher = usersService.findById(notice.getUpdatedBy());
            String userName;
            if (teacher != null) {
                userName = teacher.getLastName() + " " + teacher.getFirstName();
            } else {
                userName = notice.getUpdatedBy();
            }
            redirectAttributes.addFlashAttribute("userName", userName);
            return "redirect:/notices";
        }
        noticesService.update(id, form, user.getUserId());
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "お知らせを更新しました");
        return "redirect:/notices";
    }

    // path: /notices/{noticeId}/delete
    // お知らせを削除
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        noticesService.delete(id);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "お知らせを削除しました");
        return "redirect:/notices";
    }
}