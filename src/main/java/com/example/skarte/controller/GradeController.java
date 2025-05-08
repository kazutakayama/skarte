package com.example.skarte.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.skarte.entity.Grade;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.entity.User;
import com.example.skarte.form.GradeForm;
import com.example.skarte.service.GradeService;
import com.example.skarte.service.StudentsYearService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/grade")
//コンストラタ生成アノテーション
@RequiredArgsConstructor
public class GradeController {

    private final StudentsYearService studentsYearService;
    private final GradeService gradeService;

    // path: /grade
    // 成績一覧
    @GetMapping("")
    public String index() {
        return "grade/index";
    }

    // path: /grade
    // 成績一覧検索
    @GetMapping("/search")
    public String search(Model model, @ModelAttribute("year") Long year, @ModelAttribute("nen") Long nen,
            @ModelAttribute("kumi") Long kumi) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        model.addAttribute("studentsYear", result);
        model.addAttribute("resultSize", result.size());
        ArrayList<ArrayList<Grade>> gradeList = gradeService.gradeList(year, nen, kumi);
        model.addAttribute("grade", gradeList);
        return "grade/index";
    }

    // path: /grade/edit
    // 成績一覧編集画面を表示
    @GetMapping("/edit")
    public String edit(Model model, @ModelAttribute("year") Long year, @ModelAttribute("nen") Long nen,
            @ModelAttribute("kumi") Long kumi, @ModelAttribute("term") Long term,
            @ModelAttribute("subject") Long subject) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        model.addAttribute("studentsYear", result);
        ArrayList<ArrayList<Grade>> gradeList = gradeService.gradeList(year, nen, kumi);
        model.addAttribute("grade", gradeList);
        return "grade/edit";
    }

    // path: /grade/update
    // 成績一括更新
    @PostMapping("/update")
    public String updateGrade(RedirectAttributes redirectAttributes, Model model, GradeForm gradeForm,
            @ModelAttribute("year") Long year, @ModelAttribute("nen") Long nen, @ModelAttribute("kumi") Long kumi,
            @AuthenticationPrincipal User user) {
        gradeService.update(user.getUserId(), gradeForm);
        redirectAttributes.addFlashAttribute("year", year);
        redirectAttributes.addFlashAttribute("nen", nen);
        redirectAttributes.addFlashAttribute("kumi", kumi);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "成績を更新しました");
        return "redirect:/grade/search";
    }

}
