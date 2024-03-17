package com.example.skarte.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Karte;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.service.StudentsService;
import com.example.skarte.service.StudentsYearService;
import com.example.skarte.service.KarteService;
import com.example.skarte.service.AttendanceService;
import com.example.skarte.service.GradeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/students")
//コンストラタ生成アノテーション
@RequiredArgsConstructor
public class StudentsController {

    // コンストラクタインジェクション
    private final StudentsService studentsService;
    private final StudentsYearService studentsYearService;
    private final KarteService karteService;
    private final AttendanceService attendanceService;
    private final GradeService gradeService;

    // path: /students
    @GetMapping("")
    public String index(Model model) {
        List<Student> students = studentsService.findAll();
        model.addAttribute("students", students);
        List<StudentYear> studentsYear = studentsYearService.findAll();
        model.addAttribute("studentsYear", studentsYear);
        return "students/index";
    }

    // path: /students/{id}
    // 生徒詳細画面を表示
    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        Student students = studentsService.findById(id);
        model.addAttribute("students", students);
        List<StudentYear> studentsYear = studentsYearService.findAll();
        model.addAttribute("studentsYear", studentsYear);
        return "students/details";
    }

    // path: /students/{id}/edit
    // 生徒情報編集画面を表示
    @GetMapping("/{id}/edit")
    public String editStudent(@PathVariable Long id, Model model) {
        Student students = studentsService.findById(id);
        model.addAttribute("students", students);
        List<StudentYear> studentsYear = studentsYearService.findAll();
        model.addAttribute("studentsYear", studentsYear);
        return "students/edit";
    }

    // path: /students/{id}/update
    // 生徒情報編集画面から投稿を更新する
    @PostMapping("/{id}/update")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student) {
        Student result = studentsService.updateStudent(id, student);
        return "redirect:/students/" + result.getStudentId();
    }

    // path: /students/{id}/karte
    // カルテを表示
    @GetMapping("/{id}/karte")
    public String karte(@PathVariable Long id, Model model) {
        Student students = studentsService.findById(id);
        model.addAttribute("students", students);
        List<Karte> karte = karteService.findAll();
        model.addAttribute("karte", karte);
        return "students/karte";
    }

    // path: /students/karte/add
    // 画面で入力されたカルテを取得して、dbに登録をする
    @PostMapping("/karte/add")
    public String addKarte(@ModelAttribute Karte karte) {
        karteService.addKarte(karte);
        return "redirect:/students";
    }

    // path: /students/{id}/karte/edit
    // 編集するカルテを表示
//    @GetMapping("/{id}/karte/edit")
//    public String editKarte(@PathVariable Long id, @ModelAttribute Karte karte) {
//        Karte karte = karteService.findById(id);
//        model.addAttribute("form", post);
//        model = setList(model);
//        model.addAttribute("path", "update");
//        return "layout";
//    }

    // path: /students/karte/{id}/update
    // カルテを更新
//    @PostMapping("/karte/{id}/update")
//    public String update(@ModelAttribute("form") Post form, Model model) {
//        Optional<Post> post = repository.findById(form.getId());
//        repository.saveAndFlush(PostFactory.updatePost(post.get(), form));
//        model.addAttribute("form", PostFactory.newPost());
//        model = setList(model);
//        model.addAttribute("path", "create");
//        return "layout";
//    }

    // path: /students/karte/{id}/delete
    // カルテを削除（論理削除）
    @GetMapping("/karte/{id}/delete")
    public String deleteKarte(@PathVariable Long id, @ModelAttribute Karte karte) {
        karteService.deleteKarte(id, karte);
        return "redirect:/students";
    }

    // path: /students/{id}/attendance
    // 出欠を表示
    @GetMapping("/{id}/attendance")
    public String attendance(@PathVariable Long id, Model model) {
        Student students = studentsService.findById(id);
        model.addAttribute("students", students);
        List<Attendance> attendance = attendanceService.findAll();
        model.addAttribute("attendance", attendance);
        return "students/attendance";
    }

    // path: /students/attendance/add
    // 画面で入力された出欠を取得して、dbに登録をする
    @PostMapping("/attendance/add")
    public String addAttendance(@ModelAttribute Attendance attendance) {
        attendanceService.addAttendance(attendance);
        return "redirect:/students";
    }

    // path: /students/attendance/{id}/delete
    // 出欠を削除（論理削除）
    @GetMapping("/attendance/{id}/delete")
    public String deleteAttendance(@PathVariable Long id, @ModelAttribute Attendance attendance) {
        attendanceService.deleteAttendance(id, attendance);
        return "redirect:/students";
    }

    // path: /students/{id}/grades
    // 成績を表示
    @GetMapping("/{id}/grades")
    public String grades(@PathVariable Long id, Model model) {
        Student students = studentsService.findById(id);
        model.addAttribute("students", students);
        List<Grade> grades = gradeService.findAll();
        model.addAttribute("grades", grades);
        return "students/grades";
    }

    // path: /students/attendance/add
    // 画面で入力された成績を取得して、dbに登録をする
    @PostMapping("/grades/add")
    public String addGrade(@ModelAttribute Grade grade) {
        gradeService.addGrade(grade);
        return "redirect:/students";
    }

    // path: /students/grades/{id}/delete
    // 成績を削除（論理削除）
    @GetMapping("/grades/{id}/delete")
    public String deleteGrade(@PathVariable Long id, @ModelAttribute Grade grade) {
        gradeService.deleteGrade(id, grade);
        return "redirect:/students";
    }

}
