package com.example.skarte.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.entity.User;
import com.example.skarte.form.GradeForm;
import com.example.skarte.service.GradeService;
import com.example.skarte.service.StudentsService;
import com.example.skarte.service.StudentsYearService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/grade")
//コンストラタ生成アノテーション
@RequiredArgsConstructor
public class GradeController {

    // コンストラクタインジェクション
    private final StudentsService studentsService;
    private final StudentsYearService studentsYearService;
    private final GradeService gradeService;

    // path: /grade
    // 成績一覧
    @GetMapping("")
    public String index(Model model) {
//        List<Student> students = studentsService.findAll();
//        model.addAttribute("students", students);
//        List<StudentYear> studentsYear = studentsYearService.findAll();
//        model.addAttribute("studentsYear", studentsYear);
//        List<Grade> grade = gradeService.findAll();
//        model.addAttribute("grade", grade);
        return "grade/index";
    }

//    // path: /grade/search
//    @PostMapping("/search")
//    public String index(Model model, @RequestParam("year") Long year, @RequestParam("nen") Long nen,
//            @RequestParam("kumi") Long kumi) {
//        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
//        model.addAttribute("studentsYear", result);
//        model.addAttribute("resultSize", result.size());
//        List<Student> students = studentsService.findAll();
//        model.addAttribute("students", students);
//        List<Grade> grade = gradeService.findAll();
//        model.addAttribute("grade", grade);
//        return "grade/index";
//    }
//    
//    // path: /grade
//    // 成績一覧検索
//    @PostMapping("")
//    public String search(Model model, @RequestParam("year") Long year, @RequestParam("nen") Long nen,
//            @RequestParam("kumi") Long kumi) {
//        List<StudentYear> result = gradeService.search(year, nen, kumi);
//
//        model.addAttribute("studentsYear", result);
//        model.addAttribute("resultSize", result.size());
////        List<Student> students = studentsService.findAll();
////        model.addAttribute("students", students);
////        List<Grade> grade = gradeService.findAll();
////        model.addAttribute("grade", grade);
//////        List<Grade> grade = gradeService.grade(year);
//////        model.addAttribute("grade", grade);
//        model.addAttribute("year", year);
//        model.addAttribute("nen", nen);
//        model.addAttribute("kumi", kumi);
//        return "grade/index";
//    }

    // path: /grade
    // 成績一覧検索
    @PostMapping("")
    public String search(Model model, @RequestParam("year") Long year, @RequestParam("nen") Long nen,
            @RequestParam("kumi") Long kumi) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        model.addAttribute("studentsYear", result);
        model.addAttribute("resultSize", result.size());

        ArrayList<ArrayList<Grade>> gradeList = gradeService.gradeList(year, nen, kumi);
        model.addAttribute("grade", gradeList);
//        List<Student> students = studentsService.findAll();
//        model.addAttribute("students", students);
//        List<Grade> grade = gradeService.findAll();
//        model.addAttribute("grade", grade);
//        List<Grade> grade = gradeService.year(year);
//        model.addAttribute("grade", grade);
        model.addAttribute("year", year);
        model.addAttribute("nen", nen);
        model.addAttribute("kumi", kumi);
        return "grade/index";
    }

    // path: /grade/edit
    // 成績一覧編集画面を表示
    @PostMapping("/edit")
    public String edit(Model model, @RequestParam("year") Long year, @RequestParam("nen") Long nen,
            @RequestParam("kumi") Long kumi, @RequestParam("term") Long term, @RequestParam("subject") Long subject) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);

        model.addAttribute("studentsYear", result);
        model.addAttribute("resultSize", result.size());
//        List<Student> students = studentsService.findAll();
//        model.addAttribute("students", students);
//        List<Grade> grade = gradeService.findAll();
//        model.addAttribute("grade", grade);

        ArrayList<ArrayList<Grade>> gradeList = gradeService.gradeList(year, nen, kumi);
        model.addAttribute("grade", gradeList);
        model.addAttribute("year", year);
        model.addAttribute("nen", nen);
        model.addAttribute("kumi", kumi);
        model.addAttribute("term", term);
        model.addAttribute("subject", subject);
        return "grade/edit";
    }

    // path: /grade/update
    // 成績一括更新
    @PostMapping("/update")
    public String updateGrade(Model model, GradeForm gradeForm, @RequestParam("year") Long year,
            @RequestParam("nen") Long nen, @RequestParam("kumi") Long kumi, @AuthenticationPrincipal User user) {
        gradeService.updateGrade(user.getUserId(), gradeForm);
        model.addAttribute("year", year);
        model.addAttribute("nen", nen);
        model.addAttribute("kumi", kumi);
//        return "redirect:/grade";
//        return "grade/index"; 
        return search(model, year, nen, kumi);
    }

}
