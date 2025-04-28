package com.example.skarte.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.skarte.repository.UserRepository;
import com.example.skarte.service.AttendanceService;
import com.example.skarte.service.GradeService;
import com.example.skarte.service.ScheduleService;
import com.example.skarte.service.StudentsService;
import com.example.skarte.service.StudentsYearService;
import com.example.skarte.service.UsersService;

import lombok.RequiredArgsConstructor;

import com.example.skarte.entity.Student;
import com.example.skarte.entity.User;

@Controller
//コンストラタ生成アノテーション
@RequiredArgsConstructor
//共通処理
@ControllerAdvice
public class PagesController {

    private final UsersService usersService;

    @GetMapping("/")
    public String index() {
//        return "pages/index";
        return "redirect:/top";
    }

    @GetMapping("/top")
    public String top() {
        return "pages/index";
    }

    // 共通処理
    @ModelAttribute
    public void common(Model model, @AuthenticationPrincipal User user) {
        // ログインユーザー User
        model.addAttribute("user", user);
        // 更新者の名前のマップ lastName + FirstName
        Map<String, String> updateUser = usersService.updateUser();
        model.addAttribute("updateUser", updateUser);
        // 性別のマップ 1:男,2:女,3:他
        Map<Integer, String> gender = new HashMap<>();
        gender.put(1, "男");
        gender.put(2, "女");
        gender.put(3, "他");
        model.addAttribute("gender", gender);
        // 出欠記録のマップ 1:欠席,2:遅刻,3:早退,4:遅刻/早退,5:出停,6:忌引
        Map<Integer, String> syukketsu = new HashMap<>();
        syukketsu.put(1, "欠席");
        syukketsu.put(2, "遅刻");
        syukketsu.put(3, "早退");
        syukketsu.put(4, "遅刻/早退");
        syukketsu.put(5, "出停");
        syukketsu.put(6, "忌引");
        model.addAttribute("syukketsu", syukketsu);
        // 教科のマップ 1:国語,2:社会,3:数学,4:理科,5:音楽,6:美術,7:保健体育,8:技術家庭,9:英語
        Map<Integer, String> kyouka = new HashMap<>();
        kyouka.put(1, "国語");
        kyouka.put(2, "社会");
        kyouka.put(3, "数学");
        kyouka.put(4, "理科");
        kyouka.put(5, "音楽");
        kyouka.put(6, "美術");
        kyouka.put(7, "保健体育");
        kyouka.put(8, "技術家庭");
        kyouka.put(9, "英語");
        model.addAttribute("kyouka", kyouka);
        // 現在の「年度」       
        LocalDate date = LocalDate.now();
        int nendo;
        if (date.getMonthValue() >= 4) {
            nendo = date.getYear();
        }else {
            nendo = date.getYear() - 1;
        }        
        model.addAttribute("nendo", nendo);
        // 現在日時時刻
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年M月d日 HH:mm");
        model.addAttribute("now", now.format(dtf));
    }

}
