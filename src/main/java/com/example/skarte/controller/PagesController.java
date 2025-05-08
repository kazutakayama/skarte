package com.example.skarte.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.skarte.service.KarteService;
import com.example.skarte.service.NoticesService;
import com.example.skarte.service.StudentsYearService;
import com.example.skarte.service.UsersService;

import lombok.RequiredArgsConstructor;

import com.example.skarte.entity.Karte;
import com.example.skarte.entity.Notice;
import com.example.skarte.entity.User;

@Controller
//コンストラタ生成アノテーション
@RequiredArgsConstructor
//共通処理
@ControllerAdvice
public class PagesController {

    private final UsersService usersService;
    private final StudentsYearService studentsYearService;
    private final KarteService karteService;
    private final NoticesService noticesService;

    @GetMapping("/")
    public String index(RedirectAttributes redirectAttributes, @AuthenticationPrincipal User user) {
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message",
                "こんにちは、" + user.getLastName() + " " + user.getFirstName() + "さん");
        return "redirect:/top";
    }

    // トップページ
    @GetMapping("/top")
    public String top(Model model) {
        // 現在の「年度」
        LocalDate date = LocalDate.now();
        int nendo;
        if (date.getMonthValue() >= 4) {
            nendo = date.getYear();
        } else {
            nendo = date.getYear() - 1;
        }
        Long year = (long) nendo;
        // クラスのリスト
        ArrayList<ArrayList<Long>> yearClassList = studentsYearService.yearClassList(year);
        model.addAttribute("yearClassList", yearClassList);
        // クラスの登録人数
        ArrayList<ArrayList<Long>> classStudentsRegistered = studentsYearService.classStudentsRegistered(year);
        // クラスの在籍人数
        model.addAttribute("classStudentsRegistered", classStudentsRegistered);
        ArrayList<ArrayList<Long>> classStudentsExists = studentsYearService.classStudentsExists(year);
        model.addAttribute("classStudentsExists", classStudentsExists);
        // クラスの転出/卒業人数
        ArrayList<ArrayList<Long>> classStudentsTransferred = studentsYearService.classStudentsTransferred(year);
        model.addAttribute("classStudentsTransferred", classStudentsTransferred);
        // 合計数の計算
        ArrayList<ArrayList<Long>> classSummary = studentsYearService.classSummary(year);
        model.addAttribute("classSummary", classSummary);
        // 最近のカルテを取得
        List<Karte> recentKarte = karteService.recentKarte();
        model.addAttribute("recentKarte", recentKarte);
        // 最近のお知らせを取得
        List<Notice> recentNotice = noticesService.recentNotice();
        model.addAttribute("recentNotice", recentNotice);
        return "pages/index";
    }

    // トップページ（年度でクラス一覧・生徒数表示）
    @GetMapping("/top/year")
    public String year(Model model, @ModelAttribute("year") Long year) {
        // クラスのリスト
        ArrayList<ArrayList<Long>> yearClassList = studentsYearService.yearClassList(year);
        model.addAttribute("yearClassList", yearClassList);
        // クラスの登録人数
        ArrayList<ArrayList<Long>> classStudentsRegistered = studentsYearService.classStudentsRegistered(year);
        // クラスの在籍人数
        model.addAttribute("classStudentsRegistered", classStudentsRegistered);
        ArrayList<ArrayList<Long>> classStudentsExists = studentsYearService.classStudentsExists(year);
        model.addAttribute("classStudentsExists", classStudentsExists);
        // クラスの転出/卒業人数
        ArrayList<ArrayList<Long>> classStudentsTransferred = studentsYearService.classStudentsTransferred(year);
        model.addAttribute("classStudentsTransferred", classStudentsTransferred);
        // 合計数の計算
        ArrayList<ArrayList<Long>> classSummary = studentsYearService.classSummary(year);
        model.addAttribute("classSummary", classSummary);
        // 最近のカルテを取得
        List<Karte> recentKarte = karteService.recentKarte();
        model.addAttribute("recentKarte", recentKarte);
        // 最近のお知らせを取得
        List<Notice> recentNotice = noticesService.recentNotice();
        model.addAttribute("recentNotice", recentNotice);
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
        } else {
            nendo = date.getYear() - 1;
        }
        model.addAttribute("nendo", nendo);
        // 現在日時時刻
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年M月d日HH:mm");
        model.addAttribute("now", now.format(dtf));
    }

}
