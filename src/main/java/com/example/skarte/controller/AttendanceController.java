package com.example.skarte.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Schedule;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.entity.User;
import com.example.skarte.form.AttendanceForm;
import com.example.skarte.service.AttendanceService;
import com.example.skarte.service.ScheduleService;
import com.example.skarte.service.StudentsYearService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/attendance")
//コンストラタ生成アノテーション
@RequiredArgsConstructor
public class AttendanceController {

    private final StudentsYearService studentsYearService;
    private final AttendanceService attendanceService;
    private final ScheduleService scheduleService;

    // 出席簿一覧
    // path: /attendance
    @GetMapping("")
    public String index() {
        return "attendance/index";
    }

    // path: /attendance
    // 出席簿一覧検索
    @GetMapping("/search")
    public String search(Model model, @ModelAttribute("year") Long year, @ModelAttribute("nen") Long nen,
            @ModelAttribute("kumi") Long kumi, @ModelAttribute("month") Long month) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        model.addAttribute("studentsYear", result);
        List<Schedule> monthSchedule = scheduleService.monthSchedule(year, month);
        model.addAttribute("schedule", monthSchedule);
        ArrayList<ArrayList<Attendance>> attendanceMonth = attendanceService.attendanceMonth(year, nen, kumi, month);
        model.addAttribute("attendance", attendanceMonth);
        ArrayList<ArrayList<Integer>> attendanceMonthSummary = attendanceService.attendanceMonthSummary(year, nen, kumi,
                month);
        model.addAttribute("attendanceSummary", attendanceMonthSummary);
        return "attendance/index";
    }

    // path: /attendance/edit
    // 出席簿編集画面を表示
    @GetMapping("/edit")
    public String edit(Model model, @ModelAttribute("year") Long year, @ModelAttribute("nen") Long nen,
            @ModelAttribute("kumi") Long kumi, @ModelAttribute("month") Long month, @ModelAttribute("day") Long day) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        model.addAttribute("studentsYear", result);
        List<Schedule> monthSchedule = scheduleService.monthSchedule(year, month);
        model.addAttribute("schedule", monthSchedule);
        ArrayList<ArrayList<Attendance>> attendanceMonth = attendanceService.attendanceMonth(year, nen, kumi, month);
        model.addAttribute("attendance", attendanceMonth);
        ArrayList<ArrayList<Integer>> attendanceMonthSummary = attendanceService.attendanceMonthSummary(year, nen, kumi,
                month);
        model.addAttribute("attendanceSummary", attendanceMonthSummary);
        Calendar cal = attendanceService.editDate(year, month, day);
        model.addAttribute("cal", cal);
        return "attendance/edit";
    }

    // path: /attendance/update
    // 出席簿一括更新
    @PostMapping("/update")
    public String update(RedirectAttributes redirectAttributes, Model model, AttendanceForm attendanceForm, @ModelAttribute("year") Long year,
            @ModelAttribute("nen") Long nen, @ModelAttribute("kumi") Long kumi, @ModelAttribute("month") Long month,
            @AuthenticationPrincipal User user) {
        attendanceService.update(user.getUserId(), attendanceForm);
        redirectAttributes.addFlashAttribute("year", year);
        redirectAttributes.addFlashAttribute("nen", nen);
        redirectAttributes.addFlashAttribute("kumi", kumi);
        redirectAttributes.addFlashAttribute("month", month);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "出席簿を更新しました");
        return "redirect:/attendance/search";
    }

}
