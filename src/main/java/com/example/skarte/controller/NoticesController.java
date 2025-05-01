package com.example.skarte.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Conventions;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        // Modelに"form"が存在しない時だけ、下記の処理を実行（リダイレクトされたBindingResultが失われないようにする）
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new NoticeForm());
        }
        // Modelに"notice"が存在しない時だけ、下記の処理を実行
        if (!model.containsAttribute("notice")) {
            model.addAttribute("notice", new Notice());
        }
        return "notices/index";
    }

//    // path: /notices/new
//    // お知らせ新規登録のModalを表示
//    @GetMapping("/new")
//    public String newNotice(Model model, NoticeForm form, RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("showNewModal", true); // newModal表示
//        return "redirect:/notices";
//    }

    // path: /notices/add
    // お知らせ新規登録
    @PostMapping("/add")
    public String add(@ModelAttribute @Validated NoticeForm form, BindingResult result, Model model,
            @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
//        if (result.hasErrors()) {
//            model.addAttribute("hasMessage", true);
//            model.addAttribute("class", "alert-danger");
//            model.addAttribute("message", "登録に失敗しました");
//            model.addAttribute("showNewModal", true); // newModal表示
//            List<Notice> notices = noticesService.findAll();
//            model.addAttribute("notices", notices);
//            return "notices/index";
//        }
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

    // path: /notices/{noticeId}/edit
    // お知らせ詳細からお知らせ編集へのリンク
//    @GetMapping("/{id}/edit")
//    public String edit(@PathVariable Long id, Model model, NoticeForm form) {
//        Notice notice = noticesService.findById(id);
//        model.addAttribute("notice", notice);
//        return "notices/edit";
//    }

    // path: /notices/{noticeId}
    // お知らせ編集のModalを表示
    @GetMapping("/{id}")
    public String edit(@PathVariable Long id, Model model, NoticeForm form, RedirectAttributes redirectAttributes) {
        Notice notice = noticesService.findById(id);
        redirectAttributes.addFlashAttribute("notice", notice);
        redirectAttributes.addFlashAttribute("showEditModal", true); // editModal表示
        redirectAttributes.addFlashAttribute("id", id);
        return "redirect:/notices";
    }

//    // path: /notices/update
//    // 編集画面からお知らせを更新する
//    @PostMapping("/update")
//    public String update(@ModelAttribute @Validated NoticeForm form, BindingResult result,
//            @AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes) {
//        if (result.hasErrors()) {
//            redirectAttributes.addFlashAttribute("hasMessage", true);
//            redirectAttributes.addFlashAttribute("class", "alert-danger");
//            redirectAttributes.addFlashAttribute("message", "更新に失敗しました");
//            redirectAttributes.addFlashAttribute("showEditModal", true); // editModal表示
//            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.form", result);
//            redirectAttributes.addFlashAttribute("form", form);
//            Notice notice = noticesService.findById(form.getNoticeId());
//            redirectAttributes.addFlashAttribute("notice", notice);
//            return "redirect:/notices";
////            return "redirect:/notices/" + form.getNoticeId();
//        }
//        noticesService.update(form, user.getUserId());
//        redirectAttributes.addFlashAttribute("hasMessage", true);
//        redirectAttributes.addFlashAttribute("class", "alert-info");
//        redirectAttributes.addFlashAttribute("message", "お知らせを更新しました");
//        return "redirect:/notices";
//    }
    
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
            redirectAttributes.addFlashAttribute("id", id);
            redirectAttributes.addFlashAttribute("form", form);
            Notice notice = noticesService.findById(id);
            redirectAttributes.addFlashAttribute("notice", notice);
            return "redirect:/notices";
//            return "redirect:/notices/" + form.getNoticeId();
        }
        noticesService.update(id, form, user.getUserId());
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "お知らせを更新しました");
        return "redirect:/notices";
    }

//    // path: /notices/delete
//    // お知らせを削除（物理削除）
//    @PostMapping("/delete")
//    public String delete(@RequestParam Long noticeId, RedirectAttributes redirectAttributes) {
//        noticesService.delete(noticeId);
//        redirectAttributes.addFlashAttribute("hasMessage", true);
//        redirectAttributes.addFlashAttribute("class", "alert-info");
//        redirectAttributes.addFlashAttribute("message", "お知らせを削除しました");
//        return "redirect:/notices";
//    }
    
    // path: /notices/{noticeId}/delete
    // お知らせを削除（物理削除）
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        noticesService.delete(id);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "お知らせを削除しました");
        return "redirect:/notices";
    }

//    // path: /notices/{noticeId}/update
//    // 編集画面からお知らせを更新する
//    @PostMapping("/{id}/update")
//    public String update(@PathVariable Long id, @ModelAttribute @Validated NoticeForm form, BindingResult result,
//            @AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes) {
////        if (result.hasErrors()) {
////            model.addAttribute("hasMessage", true);
////            model.addAttribute("class", "alert-danger");
////            model.addAttribute("message", "更新に失敗しました");
////            model.addAttribute("form", form);
////            Notice notice = noticesService.findById(id);
////            model.addAttribute("notice", notice);
////            return "notices/edit";
////        }
//        if (result.hasErrors()) {
//            redirectAttributes.addFlashAttribute("hasMessage", true);
//            redirectAttributes.addFlashAttribute("class", "alert-danger");
//            redirectAttributes.addFlashAttribute("message", "更新に失敗しました");
//            redirectAttributes.addFlashAttribute("showEditModal", true); // editModal表示
//            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.form", result);
//            redirectAttributes.addFlashAttribute("form", form);
//            Notice notice = noticesService.findById(id);
//            redirectAttributes.addFlashAttribute("notice", notice);
////            return "redirect:/notices";
//            return "redirect:/notices/" + id;
//        }
//        noticesService.update(id, form, user.getUserId());
//        redirectAttributes.addFlashAttribute("hasMessage", true);
//        redirectAttributes.addFlashAttribute("class", "alert-info");
//        redirectAttributes.addFlashAttribute("message", "お知らせを更新しました");
//        return "redirect:/notices";
//    }
//
//    // path: /notices/{id}/delete
//    // お知らせを削除（物理削除）
//    @GetMapping("/{id}/delete")
//    public String delete(@PathVariable Long id, @ModelAttribute Notice notice) {
//        noticesService.delete(id);
//        return "redirect:/notices";
//    }
}