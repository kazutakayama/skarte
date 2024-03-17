package com.example.skarte.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.service.GradeService;
import com.example.skarte.service.StudentsService;
import com.example.skarte.service.StudentsYearService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/grades")
//コンストラタ生成アノテーション
@RequiredArgsConstructor
public class GradesController {

    // コンストラクタインジェクション
    private final StudentsService studentsService;
    private final StudentsYearService studentsYearService;
    private final GradeService gradeService;

    // path: /grades
    @GetMapping("")
    public String index(Model model) {
        List<Student> students = studentsService.findAll();
        model.addAttribute("students", students);
        List<StudentYear> studentsYear = studentsYearService.findAll();
        model.addAttribute("studentsYear", studentsYear);
        List<Grade> grades = gradeService.findAll();
        model.addAttribute("grades", grades);
        return "grades/index";
    }

}
