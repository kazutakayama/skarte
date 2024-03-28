package com.example.skarte.controller;

import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.skarte.entity.Notice;
import com.example.skarte.service.NoticesService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notices")
//コンストラタ生成アノテーション
@RequiredArgsConstructor
public class NoticesController {
    
    // コンストラクタインジェクション
    private final NoticesService noticesService;

//    @Autowired
//    private NoticesService noticesService;

    // path: /notices
    @GetMapping("")
    public String index(Model model) {
        List<Notice> notices = noticesService.findAll();
        model.addAttribute("notices", notices);
        return "notices/index";
    }

    // path: /notices/new
    // 新規登録ページを表示
    @GetMapping("/new")
    public String newNotice() {
        return "notices/new";
    }

    // path: /notices/add
    // 画面で入力された「タイトル」「内容」を取得して、dbに登録をする
    @PostMapping("/add")
    public String add(@ModelAttribute Notice notice) {
        noticesService.add(notice);
//        noticesService.save(notice);
        return "redirect:/notices";
    }

    // path: /notices/contents/{id}
    // お知らせ一覧からお知らせ詳細へのリンク
    @GetMapping("/contents/{id}")
    public String contents(@PathVariable Long id, Model model) {
        Notice notice = noticesService.findById(id);
        model.addAttribute("notices", notice);
        return "notices/contents";
    }

    // path: /notices/contents/{id}/edit
    // お知らせ編集画面を表示
    @GetMapping("/contents/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Notice notice = noticesService.findById(id);
        model.addAttribute("notices", notice);
        return "notices/edit";
    }

    // path: /notices/contents/{id}/update
    // 編集画面から投稿を更新する
    @PostMapping("/contents/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute Notice notice) {
        noticesService.update(id, notice);
        return "redirect:/notices";
//        Notice toUpdate = noticesService.findById(id);
//        toUpdate.setTitle(notice.getTitle());
//        toUpdate.setContents(notice.getContents());
//        noticesService.save(toUpdate);
//        return "redirect:/notices";
    }

    // path: /notices/contents/{id}/delete
    // お知らせ詳細からお知らせを削除（物理削除）
//    @GetMapping("/contents/{id}/delete")
//    public String delete(@PathVariable Long id, @ModelAttribute Notice notice) {
//        noticesService.delete(id);
//        return "redirect:/notices";
//    }

    // path: /notices/contents/{id}/delete
    // お知らせ詳細からお知らせを削除（論理削除）
    @GetMapping("/contents/{id}/delete")
    public String delete(@PathVariable Long id, @ModelAttribute Notice notice) {
        noticesService.delete(id, notice);
        return "redirect:/notices";
//        Notice toDelete = noticesService.findById(id);
//        toDelete.setDeleted(true);
//        noticesService.save(toDelete);
//        return "redirect:/notices";
    }

}
