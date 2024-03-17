package com.example.skarte.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.service.AttendanceService;
import com.example.skarte.service.StudentsService;
import com.example.skarte.service.StudentsYearService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/attendance")
//コンストラタ生成アノテーション
@RequiredArgsConstructor
public class AttendanceController {

    // コンストラクタインジェクション
    private final StudentsService studentsService;
    private final StudentsYearService studentsYearService;
    private final AttendanceService attendanceService;

    // path: /attendance
    @GetMapping("")
    public String index(Model model) {
        List<Student> students = studentsService.findAll();
        model.addAttribute("students", students);
        List<StudentYear> studentsYear = studentsYearService.findAll();
        model.addAttribute("studentsYear", studentsYear);
        List<Attendance> attendance = attendanceService.findAll();
        model.addAttribute("attendance", attendance);
        return "attendance/index";
    }

}
