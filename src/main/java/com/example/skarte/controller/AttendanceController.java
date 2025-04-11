package com.example.skarte.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Schedule;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.entity.User;
import com.example.skarte.form.AttendanceForm;
import com.example.skarte.form.GradeForm;
import com.example.skarte.service.AttendanceService;
import com.example.skarte.service.ScheduleService;
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
    private final ScheduleService scheduleService;


    // path: /attendance
    @GetMapping("")
    public String index(Model model) {
//        List<Student> students = studentsService.findAll();
//        model.addAttribute("students", students);
//        List<StudentYear> studentsYear = studentsYearService.findAll();
//        model.addAttribute("studentsYear", studentsYear);
//        List<Attendance> attendance = attendanceService.findAll();
//        model.addAttribute("attendance", attendance);
//        List<Calendar> monthCalendar = attendanceService.monthCalendar();
//        model.addAttribute("calendar", monthCalendar);

//        Calendar cal = Calendar.getInstance();
//        cal.set(2024, 3, 1);
//        model.addAttribute("cal", cal);                
//        Date date = new Date();
//        model.addAttribute("date", date);        
        return "attendance/index";
    }

    // path: /attendance
    // 出席簿一覧検索
    @PostMapping("")
    public String search(Model model, @RequestParam("year") Long year, @RequestParam("nen") Long nen,
            @RequestParam("kumi") Long kumi, @RequestParam("month") Long month) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        model.addAttribute("studentsYear", result);
        model.addAttribute("resultSize", result.size());
//        List<Calendar> monthCalendar = attendanceService.monthCalendar(year, month);
//        model.addAttribute("calendar", monthCalendar);
        
        List<Schedule> monthSchedule = scheduleService.monthSchedule(year, month);
        model.addAttribute("schedule", monthSchedule);
        int monthScheduleSize = scheduleService.monthScheduleSize(year, month);
        model.addAttribute("size", monthScheduleSize);
        

        ArrayList<ArrayList<Attendance>> attendanceMonth = attendanceService.attendanceMonth(year, nen, kumi, month);
        model.addAttribute("attendance", attendanceMonth);

        ArrayList<ArrayList<Integer>> attendanceMonthSummary = attendanceService.attendanceMonthSummary(year, nen, kumi,
                month);
        model.addAttribute("attendanceSummary", attendanceMonthSummary);

        model.addAttribute("year", year);
        model.addAttribute("nen", nen);
        model.addAttribute("kumi", kumi);
        model.addAttribute("month", month);
        return "attendance/index";
    }

    // path: /attendance/edit
    // 出席編集画面を表示
    @PostMapping("/edit")
    public String edit(Model model, @RequestParam("year") Long year, @RequestParam("nen") Long nen,
            @RequestParam("kumi") Long kumi, @RequestParam("month") Long month, @RequestParam("day") Long day) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        model.addAttribute("studentsYear", result);
        model.addAttribute("resultSize", result.size());
        List<Calendar> monthCalendar = attendanceService.monthCalendar(year, month);
        model.addAttribute("calendar", monthCalendar);
        
        List<Schedule> monthSchedule = scheduleService.monthSchedule(year, month);
        model.addAttribute("schedule", monthSchedule);
        int monthScheduleSize = scheduleService.monthScheduleSize(year, month);
        model.addAttribute("size", monthScheduleSize);

        ArrayList<ArrayList<Attendance>> attendanceMonth = attendanceService.attendanceMonth(year, nen, kumi, month);
        model.addAttribute("attendance", attendanceMonth);
        
        ArrayList<ArrayList<Integer>> attendanceMonthSummary = attendanceService.attendanceMonthSummary(year, nen, kumi,
                month);
        model.addAttribute("attendanceSummary", attendanceMonthSummary);

        model.addAttribute("year", year);
        model.addAttribute("nen", nen);
        model.addAttribute("kumi", kumi);
        model.addAttribute("month", month);
        model.addAttribute("day", day);

        // あとでサービスに書くかも
        Calendar cal = Calendar.getInstance();
        int nendo = Integer.valueOf(year.toString());
        int tsuki = Integer.valueOf(month.toString());
        int hi = Integer.valueOf(day.toString());
        if (tsuki <= 2) {
            nendo = nendo + 1;
        }
        cal.set(nendo, tsuki, hi);
        
        model.addAttribute("cal", cal);
        

        return "attendance/edit";
    }

    // path: /attendance/update
    // 出席一括更新
    @PostMapping("/update")
    public String update(Model model, AttendanceForm attendanceForm, @RequestParam("year") Long year,
            @RequestParam("nen") Long nen, @RequestParam("kumi") Long kumi, @RequestParam("month") Long month,
            @AuthenticationPrincipal User user) {
        attendanceService.update(user.getUserId(), attendanceForm);
        return "redirect:/attendance";
    }

}
