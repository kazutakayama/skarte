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
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Karte;
import com.example.skarte.entity.Schedule;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.entity.User;
import com.example.skarte.form.AttendanceForm;
import com.example.skarte.form.GradeForm;
import com.example.skarte.form.KarteForm;
import com.example.skarte.form.StudentForm;
import com.example.skarte.service.StudentsService;
import com.example.skarte.service.StudentsYearService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.skarte.service.KarteService;
import com.example.skarte.service.ScheduleService;
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
    private final ScheduleService scheduleService;

    // path: /students
    @GetMapping("")
    public String index() {
        return "students/index";
    }

    // path: /students/search
    // クラス検索
    @GetMapping("/search")
    public String search(Model model, @ModelAttribute("year") Long year, @ModelAttribute("nen") Long nen,
            @ModelAttribute("kumi") Long kumi) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        model.addAttribute("studentsYear", result);
        model.addAttribute("resultSize", result.size());
        List<Student> students = studentsService.findAll();
        model.addAttribute("students", students);
        return "students/index";
    }

    // path: /students/download.csv
    // クラス検索後、クラス（学年）ごとのリストをCSVダウンロード
    @GetMapping(value = "/download.csv", params = "download_file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
            + "; charset=UTF-8; Content-Disposition: attachment")
    @ResponseBody
    public Object download(@RequestParam("year") Long year, @RequestParam("nen") Long nen,
            @RequestParam("kumi") Long kumi) throws JsonProcessingException {
        return studentsService.downloadClass(year, nen, kumi);
    }

    // path: /students/{studentId}
    // 生徒情報を表示
    @GetMapping("/{id}")
    public String details(@PathVariable String id, Model model) {
        Student student = studentsService.findById(id);
        model.addAttribute("student", student);
        List<StudentYear> classList = studentsYearService.classList(id);
        model.addAttribute("studentsYear", classList);
        return "students/details";
    }

    // path: /students/{studentId}/edit
    // 生徒編集画面を表示
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable String id, Model model, @ModelAttribute StudentForm form) {
        Student student = studentsService.findById(id);
        model.addAttribute("student", student);
        return "students/edit";
    }

//    // path: /students/{studentId}/update
//    // 生徒を更新する
//    @PostMapping("/{id}/update")
//    public String updateStudent(@PathVariable String id, @ModelAttribute Student student) {
//        Student result = studentsService.updateStudent(id, student);
//        return "redirect:/students/" + result.getStudentId();
//    }

    // path: /students/{studentId}/update
    // 生徒編集画面から生徒を更新する
    @PostMapping("/{id}/update")
    public String update(@PathVariable String id, @Validated @ModelAttribute StudentForm form, BindingResult result,
            Model model, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "更新に失敗しました");
            Student student = studentsService.findById(id);
            model.addAttribute("student", student);
            return "students/edit";
        }
        studentsService.update(id, form, user.getUserId());
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "生徒を更新しました");
        return "redirect:/students/{id}";
    }

    // path: /students/{studentId}/karte
    // カルテを表示
    @GetMapping("/{id}/karte")
    public String karte(@PathVariable String id, Model model, @ModelAttribute KarteForm karteForm) {
        Student student = studentsService.findById(id);
        model.addAttribute("student", student);
        List<Karte> karte = karteService.findAllByStudentId(id);
        model.addAttribute("karte", karte);
        return "students/karte";
    }

    // path: /students/{studentId}/karte/add
    // 画面で入力されたカルテを取得して、dbに登録をする
    @PostMapping("/{id}/karte/add")
    public String addKarte(@PathVariable String id, @Validated @ModelAttribute KarteForm karteForm,
            BindingResult result, @AuthenticationPrincipal User user, Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Student student = studentsService.findById(id);
            model.addAttribute("student", student);
            List<Karte> karte = karteService.findAllByStudentId(id);
            model.addAttribute("karte", karte);
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "登録に失敗しました");
            return "students/karte";
        }
        karteService.add(id, user.getUserId(), karteForm);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "カルテを登録しました");
        return "redirect:/students/" + id + "/karte";
    }

    // path: /students/{karteId}/karte/edit
    // 編集するカルテを表示
    @GetMapping("/{id}/karte/edit")
    public String editKarte(@PathVariable Long id, Model model, @ModelAttribute KarteForm karteForm) {
        Karte karte = karteService.findById(id);
        model.addAttribute("karte", karte);
        Student student = studentsService.findById(karte.getStudentId());
        model.addAttribute("student", student);
        return "students/editKarte";
    }

    // path: /students/{karteId}/karte/update
    // カルテを更新
    @PostMapping("/{id}/karte/update")
    public String updateKarte(@PathVariable Long id, @AuthenticationPrincipal User user,
            @Validated @ModelAttribute KarteForm karteForm, BindingResult result, RedirectAttributes redirectAttributes,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "更新に失敗しました");
            Karte karte = karteService.findById(id);
            model.addAttribute("karte", karte);
            Student student = studentsService.findById(karte.getStudentId());
            model.addAttribute("student", student);
            return "students/editKarte";
        }
        karteService.update(id, user.getUserId(), karteForm);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "カルテを更新しました");
        return "redirect:/students/" + karteForm.getStudentId() + "/karte";
    }

    // path: /students/{karteId}/kartedelete
    // カルテを削除
    @GetMapping("/{id}/karte/delete")
    public String deleteKarte(@PathVariable Long id) {
        Karte karte = karteService.findById(id);
        karteService.delete(id);
        return "redirect:/students/" + karte.getStudentId() + "/karte";
    }

    // path: /students/{studentId}/attendance
    // 生徒の出席簿（3年分まとめ）を表示
    @GetMapping("/{id}/attendance")
    public String attendance(@PathVariable String id, Model model) {
        Student student = studentsService.findById(id);
        model.addAttribute("student", student);
        List<StudentYear> studentsYear = studentsYearService.findAllByStudentId(id);
        model.addAttribute("studentsYear", studentsYear);
        
        ArrayList<ArrayList<ArrayList<Integer>>> studentAttendanceSummary = attendanceService
                .studentAttendanceSummary(id);
        model.addAttribute("attendanceSummary", studentAttendanceSummary);
        // 3年分のまとめ
        List<Integer> studentAttendanceTotal = attendanceService.studentAttendanceTotal(id);
        model.addAttribute("AttendanceTotal", studentAttendanceTotal);
        return "students/attendance";
    }

    // path: /students/{studentId}/attendance/edit
    // 生徒の出席簿（月ごと・編集用）を表示
    @GetMapping("/{id}/attendance/edit")
    public String editAttendance(@PathVariable String id, Model model, @ModelAttribute("year") Long year,
            @ModelAttribute("month") Long month) {
        Student student = studentsService.findById(id);
        model.addAttribute("student", student);
        List<StudentYear> studentsYear = studentsYearService.findAllByStudentId(id);
        model.addAttribute("studentsYear", studentsYear);

        List<Schedule> monthSchedule = scheduleService.monthSchedule(year, month);
        model.addAttribute("schedule", monthSchedule);

        List<Attendance> studentAttendanceMonth = attendanceService.studentAttendanceMonth(id, year, month);
        model.addAttribute("attendance", studentAttendanceMonth);
        List<Integer> studentAttendanceMonthSummary = attendanceService.studentAttendanceMonthSummary(id, year, month);
        model.addAttribute("attendanceSummary", studentAttendanceMonthSummary);
        return "students/editAttendance";
    }

    // path: /students/{studentId}/attendance/update
    // 出席簿一括更新
    @PostMapping("/{id}/attendance/update")
    public String updateAttendance(@PathVariable String id, Model model, AttendanceForm attendanceForm,
            @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        attendanceService.update(user.getUserId(), attendanceForm);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "出席簿を更新しました");
        return "redirect:/students/{id}/attendance";
    }

//    // path: /students/attendance/{id}/delete
//    // 出欠を削除（論理削除）
//    @GetMapping("/attendance/{id}/delete")
//    public String deleteAttendance(@PathVariable Long id, @ModelAttribute Attendance attendance) {
//        attendanceService.deleteAttendance(id, attendance);
//        return "redirect:/students";
//    }

    // path: /students/{studentId}/grade
    // 生徒の成績を表示
    @GetMapping("/{id}/grade")
    public String grade(@PathVariable String id, Model model) {
        Student student = studentsService.findById(id);
        model.addAttribute("student", student);
        List<StudentYear> studentsYear = studentsYearService.findAllByStudentId(id);
        model.addAttribute("studentsYear", studentsYear);
        ArrayList<ArrayList<Grade>> studentGradeAll = gradeService.studentGradeAll(id);
        model.addAttribute("grade", studentGradeAll);

        return "students/grade";
    }

    // path: /students/{studentId}/grade/edit
    // 生徒の成績（年度ごと・編集用）を表示
    @GetMapping("/{id}/grade/edit")
    public String editGrade(@PathVariable String id, Model model, @ModelAttribute("year") Long year) {
        Student student = studentsService.findById(id);
        model.addAttribute("student", student);
        List<StudentYear> studentsYear = studentsYearService.findAllByStudentId(id);
        model.addAttribute("studentsYear", studentsYear);
        ArrayList<ArrayList<Grade>> studentGradeAll = gradeService.studentGradeAll(id);
        model.addAttribute("grade", studentGradeAll);
        return "students/editGrade";
    }

    // path: /students/{studentId}/grade/update
    // 成績一括更新
    @PostMapping("/{id}/grade/update")
    public String updateGrade(@PathVariable String id, GradeForm gradeForm, @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) {
        gradeService.update(user.getUserId(), gradeForm);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "成績を更新しました");
        return "redirect:/students/{id}/grade";
    }

//    // path: /students/grade/{id}/delete
//    // 成績を削除（論理削除）
//    @GetMapping("/grade/{id}/delete")
//    public String deleteGrade(@PathVariable Long id, @ModelAttribute Grade grade) {
//        gradeService.deleteGrade(id, grade);
//        return "redirect:/students";
//    }

}
