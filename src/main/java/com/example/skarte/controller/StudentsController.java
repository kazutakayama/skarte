//package com.example.skarte.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.example.skarte.entity.Attendance;
//import com.example.skarte.entity.Grade;
//import com.example.skarte.entity.Karte;
//import com.example.skarte.entity.Student;
//import com.example.skarte.entity.StudentYear;
//import com.example.skarte.service.StudentsService;
//import com.example.skarte.service.StudentsYearService;
//import com.example.skarte.service.KarteService;
//import com.example.skarte.service.AttendanceService;
//import com.example.skarte.service.GradeService;
//
//@Controller
//@RequestMapping("/students")
//
//public class StudentsController {
//
//    
//    @Autowired
//    private StudentsService studentsService;
//    private StudentsYearService studentsYearService;
//    private KarteService karteService;
//    private AttendanceService attendanceService;
//    private GradeService gradeService;
//    
//
//    // path: /students
//    @GetMapping("")
//    public String index(Model model) {
//        List<Student> students = studentsService.findAll();
//        model.addAttribute("students", students);
////        List<StudentYear> studentsYear = studentsYearService.findAll();
////        model.addAttribute("studentsYear", studentsYear);
//        return "students/index";
//    }
//
//    // path: /students/{id}
//    // 生徒詳細画面を表示
//    @GetMapping("/{id}")
//    public String details(@PathVariable Long id, Model model) {
//        Student student = studentsService.findById(id);
//        model.addAttribute("students", student);
////        List<StudentYear> studentsYear = studentsYearService.findAllByStudentId(id);
////        model.addAttribute("studentsYear", studentsYear);
//        return "students/details";
//    }
//
//    // path: /students/{id}/edit
//    // 生徒情報編集画面を表示
//    @GetMapping("/{id}/edit")
//    public String editStudent(@PathVariable Long id, Model model) {
//        Student student = studentsService.findById(id);
//        model.addAttribute("students", student);
//        List<StudentYear> studentsYear = studentsYearService.findAll();
//        model.addAttribute("studentsYear", studentsYear);
//        return "students/edit";
//    }
//
////    // path: /students/{id}/update
////    // 生徒情報編集画面から生徒情報を更新する
////    @PostMapping("/{id}/update")
////    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student) {
////        Student result = studentsService.updateStudent(id, student);
////        return "redirect:/students/" + result.getStudentId();
////    }
//
//    // path: /students/{id}/karte
//    // カルテを表示
//    @GetMapping("/{id}/karte")
//    public String karte(@PathVariable Long id, Model model) {
//        Student student = studentsService.findById(id);
//        model.addAttribute("students", student);
//        List<Karte> karte = karteService.findAllByStudentId(id);
//        model.addAttribute("karte", karte);
//        return "students/karte";
//    }
//
//    // path: /students/karte/add
//    // 画面で入力されたカルテを取得して、dbに登録をする
//    @PostMapping("/karte/add")
//    public String addKarte(@ModelAttribute Karte karte) {
//        karteService.addKarte(karte);
//        return "redirect:/students";
//    }
//
//    // path: /students/{id}/karte/edit
//    // 編集するカルテを表示
////    @GetMapping("/{id}/karte/edit")
////    public String editKarte(@PathVariable Long id, @ModelAttribute Karte karte) {
////        Karte karte = karteService.findById(id);
////        model.addAttribute("form", post);
////        model = setList(model);
////        model.addAttribute("path", "update");
////        return "layout";
////    }
//
//    // path: /students/karte/{id}/update
//    // カルテを更新
////    @PostMapping("/karte/{id}/update")
////    public String update(@ModelAttribute("form") Post form, Model model) {
////        Optional<Post> post = repository.findById(form.getId());
////        repository.saveAndFlush(PostFactory.updatePost(post.get(), form));
////        model.addAttribute("form", PostFactory.newPost());
////        model = setList(model);
////        model.addAttribute("path", "create");
////        return "layout";
////    }
//
////    // path: /students/karte/{id}/delete
////    // カルテを削除（論理削除）
////    @GetMapping("/karte/{id}/delete")
////    public String deleteKarte(@PathVariable Long id, @ModelAttribute Karte karte) {
////        karteService.deleteKarte(id, karte);
////        return "redirect:/students";
////    }
//
//    // path: /students/{id}/attendance
//    // 出欠を表示
//    @GetMapping("/{id}/attendance")
//    public String attendance(@PathVariable Long id, Model model) {
//        Student student = studentsService.findById(id);
//        model.addAttribute("students", student);
//        List<Attendance> attendance = attendanceService.findAllByStudentId(id);
//        model.addAttribute("attendance", attendance);
//        return "students/attendance";
//    }
//
//    // path: /students/attendance/add
//    // 画面で入力された出欠を取得して、dbに登録をする
//    @PostMapping("/attendance/add")
//    public String addAttendance(@ModelAttribute Attendance attendance) {
//        attendanceService.addAttendance(attendance);
//        return "redirect:/students";
//    }
//
////    // path: /students/attendance/{id}/delete
////    // 出欠を削除（論理削除）
////    @GetMapping("/attendance/{id}/delete")
////    public String deleteAttendance(@PathVariable Long id, @ModelAttribute Attendance attendance) {
////        attendanceService.deleteAttendance(id, attendance);
////        return "redirect:/students";
////    }
//
//    // path: /students/{id}/grades
//    // 成績を表示
//    @GetMapping("/{id}/grades")
//    public String grades(@PathVariable Long id, Model model) {
//        Student student = studentsService.findById(id);
//        model.addAttribute("students", student);      
//        List<Grade> grades = gradeService.findAllByStudentId(id);
//        model.addAttribute("grades", grades);
//        return "students/grades";
//    }
//
//    // path: /students/attendance/add
//    // 画面で入力された成績を取得して、dbに登録をする
//    @PostMapping("/grades/add")
//    public String addGrade(@ModelAttribute Grade grade) {
//        gradeService.addGrade(grade);
//        return "redirect:/students";
//    }
//
////    // path: /students/grades/{id}/delete
////    // 成績を削除（論理削除）
////    @GetMapping("/grades/{id}/delete")
////    public String deleteGrade(@PathVariable Long id, @ModelAttribute Grade grade) {
////        gradeService.deleteGrade(id, grade);
////        return "redirect:/students";
////    }
//
//}

package com.example.skarte.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Karte;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.entity.User;
import com.example.skarte.form.GradeForm;
import com.example.skarte.form.StudentForm;
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

//    @Autowired
//    private StudentsService studentsService;
//    private StudentsYearService studentsYearService;
//    private KarteService karteService;
//    private AttendanceService attendanceService;
//    private GradeService gradeService;

    // path: /students
    @GetMapping("")
    public String index(Model model) {
        List<Student> students = studentsService.findAll();
        model.addAttribute("students", students);
        List<StudentYear> studentsYear = studentsYearService.findAll();
        model.addAttribute("studentsYear", studentsYear);
        return "students/index";
    }

    // path: /students/class
    // クラス検索
    @PostMapping("/search")
    public String search(Model model, @RequestParam("year") Long year, @RequestParam("nen") Long nen,
            @RequestParam("kumi") Long kumi) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        model.addAttribute("studentsYear", result);
        model.addAttribute("resultSize", result.size());
        List<Student> students = studentsService.findAll();
        model.addAttribute("students", students);
        return "students/index";
    }

    // path: /students/{studentId}
    // 生徒詳細画面を表示
    @GetMapping("/{id}")
    public String details(@PathVariable String id, Model model) {
        Student student = studentsService.findById(id);
        model.addAttribute("students", student);
        List<StudentYear> studentsYear = studentsYearService.findAllByStudentId(id);
        model.addAttribute("studentsYear", studentsYear);
        return "students/details";
    }

    // path: /students/{studentId}/edit
    // 生徒編集画面を表示
    @GetMapping("/{id}/edit")
    public String editStudent(@PathVariable String id, Model model) {
        Student student = studentsService.findById(id);
        model.addAttribute("students", student);
        List<StudentYear> studentsYear = studentsYearService.findAll();
        model.addAttribute("studentsYear", studentsYear);
        return "students/edit";
    }

//    // path: /students/{studentId}/update
//    // 生徒を更新する
//    @PostMapping("/{id}/update")
//    public String updateStudent(@PathVariable String id, @ModelAttribute Student student) {
//        Student result = studentsService.updateStudent(id, student);
//        return "redirect:/students/" + result.getStudentId();
//    }

    // path: /students/{studentId}/updatestudent
    // 生徒編集画面から生徒を更新する
    @PostMapping("/{id}/updateStudent")
    public String updateStudent(@PathVariable String id, @Validated @ModelAttribute StudentForm form,
            @AuthenticationPrincipal User user) {
        form.setUpdatedBy(user.getUserId());
        Student result = studentsService.updateStudent(id, form);
        return "redirect:/students/" + result.getStudentId();
    }

    // path: /students/{studentId}/karte
    // カルテを表示
    @GetMapping("/{id}/karte")
    public String karte(@PathVariable String id, Model model) {
        Student student = studentsService.findById(id);
        model.addAttribute("students", student);
        List<Karte> karte = karteService.findAllByStudentId(id);
        model.addAttribute("karte", karte);
        return "students/karte";
    }

    // path: /students/karte/add
    // 画面で入力されたカルテを取得して、dbに登録をする
    @PostMapping("/karte/add")
    public String addKarte(@ModelAttribute Karte karte, @AuthenticationPrincipal User user) {
        karteService.addKarte(user.getUserId(), karte.getStudentId(), karte);
        return "redirect:/students/" + karte.getStudentId() + "/karte";
    }

    // path: /students/{karteId}/editKarte
    // 編集するカルテを表示
    @GetMapping("/{id}/editKarte")
    public String editKarte(@PathVariable Long id, Model model) {
        Karte karte = karteService.findById(id);
        model.addAttribute("karte", karte);
        Student student = studentsService.findById(karte.getStudentId());
        model.addAttribute("students", student);
        return "students/editKarte";
    }

    // path: /students/{karteId}/updateKarte
    // カルテを更新
    @PostMapping("/{id}/updateKarte")
    public String updateKarte(@PathVariable Long id, @ModelAttribute Karte karte, @AuthenticationPrincipal User user) {
        karte.setUpdatedBy(user.getUserId());
        Karte result = karteService.updateKarte(id, karte.getStudentId(), karte);
        return "redirect:/students/" + result.getStudentId() + "/karte";
    }

    // path: /students/{karteId}/deleteKarte
    // カルテを削除
    @GetMapping("/{id}/deleteKarte")
    public String deleteKarte(@PathVariable Long id, @ModelAttribute Karte karte) {
        Karte deleteKarte = karteService.deleteKarte(id, karte.getStudentId(), karte);
        return "redirect:/students/" + deleteKarte.getStudentId() + "/karte";
    }

//    // path: /students/{id}/deleteKarte
//    // カルテを削除
//    @GetMapping("/{id}/deleteKarte")
//    public String deleteKarte(@PathVariable Long id, @ModelAttribute Student student) {
//        karteService.deleteKarte(id);
//        return "redirect:/students";
//    }

//    // path: /students/karte/{id}/delete
//    // カルテを削除（論理削除）
//    @GetMapping("/karte/{id}/delete")
//    public String deleteKarte(@PathVariable Long id, @ModelAttribute Karte karte) {
//        karteService.deleteKarte(id, karte);
//        return "redirect:/students";
//    }

    // path: /students/{studentId}/attendance
    // 出欠を表示
    @GetMapping("/{id}/attendance")
    public String attendance(@PathVariable String id, Model model) {
        Student student = studentsService.findById(id);
        model.addAttribute("students", student);
        List<Attendance> attendance = attendanceService.findAllByStudentId(id);
        model.addAttribute("attendance", attendance);
        return "students/attendance";
    }

    // path: /students/attendance/add
    // 画面で入力された出欠を取得して、dbに登録をする
    @PostMapping("/attendance/add")
    public String addAttendance(@ModelAttribute Attendance attendance, @AuthenticationPrincipal User user) {
        attendanceService.addAttendance(user.getUserId(), attendance.getStudentId(), attendance);
        return "redirect:/students/" + attendance.getStudentId() + "/attendance";
    }

    // path: /students/{attendanceId}/editAttendance
    // 編集する出欠を表示
    @GetMapping("/{id}/editAttendance")
    public String editAtendance(@PathVariable Long id, Model model) {
        Attendance attendance = attendanceService.findById(id);
        model.addAttribute("attendance", attendance);
        Student student = studentsService.findById(attendance.getStudentId());
        model.addAttribute("students", student);
        return "students/editAttendance";
    }

    // path: /students/{attendanceId}/updateAttendance
    // 出欠を更新
    @PostMapping("/{id}/updateAttendance")
    public String updateAttendance(@PathVariable Long id, @ModelAttribute Attendance attendance,
            @AuthenticationPrincipal User user) {
        attendance.setUpdatedBy(user.getUserId());
        Attendance result = attendanceService.updateAttendance(id, attendance.getStudentId(), attendance);
        return "redirect:/students/" + result.getStudentId() + "/attendance";
    }

    // path: /students/{attendanceId}/deleteAttendance
    // 出欠を削除
    @GetMapping("/{id}/deleteAttendance")
    public String deleteAttendance(@PathVariable Long id, @ModelAttribute Attendance attendance) {
        Attendance deleteAttendance = attendanceService.deleteAttendance(id, attendance.getStudentId(), attendance);
        return "redirect:/students/" + deleteAttendance.getStudentId() + "/attendance";
    }

//    // path: /students/{karteId}/deleteKarte
//    // カルテを削除
//    @GetMapping("/{id}/deleteKarte")
//    public String deleteKarte(@PathVariable Long id, @ModelAttribute Karte karte) {
//        Karte deleteKarte = karteService.deleteKarte(id, karte.getStudentId(), karte);
//        return "redirect:/students/" + deleteKarte.getStudentId() + "/karte";
//    }

//    // path: /students/attendance/{id}/delete
//    // 出欠を削除（論理削除）
//    @GetMapping("/attendance/{id}/delete")
//    public String deleteAttendance(@PathVariable Long id, @ModelAttribute Attendance attendance) {
//        attendanceService.deleteAttendance(id, attendance);
//        return "redirect:/students";
//    }

    // path: /students/{studentId}/grade
    // 成績を表示
    @GetMapping("/{id}/grade")
    public String grade(@PathVariable String id, Model model) {
        Student student = studentsService.findById(id);
        model.addAttribute("students", student);
        List<StudentYear> studentsYear = studentsYearService.findAllByStudentId(id);
        model.addAttribute("studentsYear", studentsYear);
        ArrayList<ArrayList<Grade>> studentGradeAll = gradeService.studentGradeAll(id);
        model.addAttribute("grade", studentGradeAll);

        return "students/grade";
    }

    // path: /students/grade/add
    // 画面で入力された成績を取得して、dbに登録をする
    @PostMapping("/grade/add")
    public String addGrade(@ModelAttribute Grade grade, @AuthenticationPrincipal User user) {
        gradeService.addGrade(user.getUserId(), grade.getStudentId(), grade);
        return "redirect:/students/" + grade.getStudentId() + "/grade";
    }

    // path: /students/{studentId}/grade/edit
    // 一括編集する成績（年度ごと）を表示
    @PostMapping("/{id}/grade/edit")
    public String editStudentGrade(@PathVariable String id, Model model, @RequestParam("year") Long year,
            GradeForm gradeForm) {
        Student student = studentsService.findById(id);
        model.addAttribute("students", student);
        List<StudentYear> studentsYear = studentsYearService.findAllByStudentId(id);
        model.addAttribute("studentsYear", studentsYear);
        ArrayList<ArrayList<Grade>> studentGradeAll = gradeService.studentGradeAll(id);
        model.addAttribute("grade", studentGradeAll);
        model.addAttribute("year", year);
        return "students/editStudentGrade";
    }

    // path: /students/{gradeId}/editGrade
    // 編集する成績を表示
    @GetMapping("/{id}/editGrade")
    public String editGrade(@PathVariable Long id, Model model) {
        Grade grade = gradeService.findById(id);
        model.addAttribute("grade", grade);
        Student student = studentsService.findById(grade.getStudentId());
        model.addAttribute("students", student);
        List<StudentYear> studentsYear = studentsYearService.findAllByStudentId(grade.getStudentId());
        model.addAttribute("studentsYear", studentsYear);
        return "students/editGrade";
    }

    // path: /students/{gradeId}/updateGrade
    // 成績を更新
    @PostMapping("/{id}/updateGrade")
    public String updateGradeOne(@PathVariable Long id, @ModelAttribute Grade grade,
            @AuthenticationPrincipal User user) {
        grade.setUpdatedBy(user.getUserId());
        Grade result = gradeService.updateGradeOne(id, grade.getStudentId(), grade);
        return "redirect:/students/" + result.getStudentId() + "/grade";
    }

    // path: /students/{studentId}/grade/update
    // 成績一括更新
    @PostMapping("/{id}/grade/update")
    public String updateGrade(@PathVariable String id, GradeForm gradeForm, @AuthenticationPrincipal User user) {
        gradeService.updateGrade(user.getUserId(), gradeForm);
        return "redirect:/students/" + id + "/grade";
    }

    // path: /students/{gradeId}/deleteGrade
    // 成績を削除
    @GetMapping("/{id}/deleteGrade")
    public String deleteGrade(@PathVariable Long id, @ModelAttribute Grade grade) {
        Grade deleteGrade = gradeService.deleteGrade(id, grade.getStudentId(), grade);
        return "redirect:/students/" + deleteGrade.getStudentId() + "/grade";
    }

//    // path: /students/grade/{id}/delete
//    // 成績を削除（論理削除）
//    @GetMapping("/grade/{id}/delete")
//    public String deleteGrade(@PathVariable Long id, @ModelAttribute Grade grade) {
//        gradeService.deleteGrade(id, grade);
//        return "redirect:/students";
//    }

}
