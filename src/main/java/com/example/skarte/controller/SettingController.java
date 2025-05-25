package com.example.skarte.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.skarte.entity.Schedule;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.entity.User;
import com.example.skarte.form.ScheduleForm;
import com.example.skarte.form.StudentForm;
import com.example.skarte.form.StudentYearForm;
import com.example.skarte.form.UserEditForm;
import com.example.skarte.service.ScheduleService;
import com.example.skarte.service.StudentsService;
import com.example.skarte.service.StudentsYearService;
import com.example.skarte.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/setting")
// コンストラタ生成アノテーション
@RequiredArgsConstructor
public class SettingController {

    // コンストラクタインジェクション
    private final StudentsService studentsService;
    private final StudentsYearService studentsYearService;
    private final ScheduleService scheduleService;
    private final UsersService usersService;

    // path: /setting
    @GetMapping("")
    public String index() {
        return "setting/index";
    }

    // path: /setting/students
    // 生徒管理ページを表示
    @GetMapping("/students")
    public String students(Model model) {
        List<Student> students = studentsService.findAll();
        model.addAttribute("students", students);
        return "setting/students/students";
    }

    // path: /setting/students/search
    // 生徒検索（生徒名または登録年度）
    @GetMapping("/students/search")
    public String search(Model model, @ModelAttribute("name") String name, @ModelAttribute("year") String year) {
        List<Student> result = studentsService.search(name, year);
        model.addAttribute("students", result);
        return "setting/students/students";
    }

    // path: /setting/students/new
    // 生徒新規登録ページを表示
    @GetMapping("students/new")
    public String newStudent(@ModelAttribute StudentForm form) {
        return "setting/students/new";
    }

    // path: /setting/students/add
    // 画面で入力された生徒情報を取得して、dbに登録をする
    @PostMapping("/students/add")
    public String add(@Validated @ModelAttribute StudentForm form, BindingResult result, Model model,
            @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        // ※BindingResultは必ずバリデーション対象オブジェクトの直後に置く
        if (studentsService.findByStudentId(form.getStudentId()) != null) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "その生徒IDはすでに登録済みです");
            model.addAttribute("form", form);
            return "setting/students/new";
        }
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "登録に失敗しました");
            model.addAttribute("form", form);
            return "setting/students/new";
        }
        studentsService.add(user.getUserId(), form);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "生徒を登録しました");
        return "redirect:/setting/students/" + form.getStudentId();
    }

    // path: /setting/students/{studentId}
    // 生徒詳細画面を表示
    @GetMapping("/students/{id}")
    public String detailsAndClassList(@PathVariable String id, Model model) {
        Student student = studentsService.findById(id);
        model.addAttribute("student", student);
        List<StudentYear> classList = studentsYearService.classList(id);
        model.addAttribute("studentsYear", classList);
        List<String> images = studentsYearService.images(id);
        model.addAttribute("images", images);
        return "setting/students/details";
    }

    // path: /setting/students/{studentId}/edit
    // 生徒情報編集画面を表示
    @GetMapping("/students/{id}/edit")
    public String edit(@PathVariable String id, Model model, @ModelAttribute StudentForm form) {
        Student student = studentsService.findById(id);
        model.addAttribute("student", student);
        boolean dataExists = studentsService.dataExists(id);
        model.addAttribute("dataExists", dataExists);
        return "setting/students/edit";
    }

    // path: /setting/students/{id}/update
    // 生徒情報編集画面から生徒情報を更新する
    @PostMapping("/students/{id}/update")
    public String update(@PathVariable String id, @Validated @ModelAttribute StudentForm form, BindingResult result,
            Model model, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "更新に失敗しました");
            Student student = studentsService.findById(id);
            model.addAttribute("student", student);
            boolean dataExists = studentsService.dataExists(id);
            model.addAttribute("dataExists", dataExists);
            return "setting/students/edit";
        }
        studentsService.update(id, form, user.getUserId());
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "生徒を更新しました");
        return "redirect:/setting/students/" + id;
    }

    // path: /setting/students/download.csv
    // 名前、年度で絞り込み後、生徒をcsvでダウンロードする
    @GetMapping(value = "/students/download.csv", params = "download_file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
            + "; charset=UTF-8; Content-Disposition: attachment")
    @ResponseBody
    public Object download(@RequestParam("name") String name, @RequestParam("year") String year)
            throws JsonProcessingException {
        return studentsService.download(name, year);
    }

    // path: /setting/students/upload
    // 生徒をcsvでアップロードする
    @PostMapping(value = "/students/upload", params = "upload_file")
    public String uploadFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user, Model model,
            StudentForm form, RedirectAttributes redirectAttributes) throws Exception {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<Student> studentList = studentsService.upload(user.getUserId(), br);
            if (studentList.size() != 0) {
                redirectAttributes.addFlashAttribute("hasMessage", true);
                redirectAttributes.addFlashAttribute("class", "alert-info");
                redirectAttributes.addFlashAttribute("message", studentList.size() + "人の生徒を登録しました");
                return "redirect:/setting/students";
            } else {
                model.addAttribute("hasMessage", true);
                model.addAttribute("class", "alert-danger");
                model.addAttribute("message", "登録に失敗しました。入力必須項目や、生徒IDの重複などを確認してください");
                return "setting/students/new";
            }
        } catch (IOException e) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "登録に失敗しました。ファイルが読み込みできません" + " " + e);
            return "setting/students/new";
        } catch (Exception e) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "登録に失敗しました" + " " + e);
            return "setting/students/new";
        }
    }

    // path: /setting/students/{id}/delete
    // 設定/生徒を削除
    @GetMapping("/students/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        studentsService.delete(id);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "生徒を削除しました");
        return "redirect:/setting/students";
    }

    // path: /setting/class
    // クラス管理ページを表示
    @GetMapping("/class")
    public String classTop(Model model) {
        return "setting/class/class";
    }

    // path: /setting/class/list
    // クラスを検索
    @GetMapping("/class/list")
    public String classList(Model model, @ModelAttribute("year") Long year, @ModelAttribute("nen") Long nen,
            @ModelAttribute("kumi") Long kumi) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        model.addAttribute("studentsYear", result);
        if (result.size() != 0) {
            List<String> imageList = studentsYearService.imageList(result);
            model.addAttribute("images", imageList);
        }
        List<StudentYear> transferred = studentsYearService.transferred(year, nen, kumi);
        model.addAttribute("transferred", transferred);
        List<StudentYear> exists = studentsYearService.exists(year, nen, kumi);
        model.addAttribute("exists", exists);
        return "setting/class/class";
    }

    // path: /setting/class/register
    // クラス一括登録画面を表示
    @GetMapping("/class/register")
    public String registerClass(Model model, @ModelAttribute("year") Long year, @ModelAttribute("nen") Long nen,
            @ModelAttribute("kumi") Long kumi) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        model.addAttribute("studentsYear", result);
        List<Student> studentsOption = studentsYearService.studentsOption(year, nen);
        model.addAttribute("students", studentsOption);
        return "setting/class/register";
    }

    // path: /setting/class/create
    // クラス一括登録画面から生徒一括追加
    @PostMapping("/class/create")
    public String createClass(RedirectAttributes redirectAttributes, Model model, @ModelAttribute("year") Long year,
            @ModelAttribute("nen") Long nen, @ModelAttribute("kumi") Long kumi, StudentYearForm studentYearForm,
            @AuthenticationPrincipal User user) {
        boolean isDuplicated = studentsYearService.isDuplicated(studentYearForm, year, nen, kumi);
        if (isDuplicated == false) {
            List<String> create = studentsYearService.create(user.getUserId(), studentYearForm, year, nen, kumi);
            studentsYearService.sort(year, nen, kumi, user.getUserId());
            redirectAttributes.addFlashAttribute("year", year);
            redirectAttributes.addFlashAttribute("nen", nen);
            redirectAttributes.addFlashAttribute("kumi", kumi);
            if (create != null) {
                redirectAttributes.addFlashAttribute("hasMessage", true);
                redirectAttributes.addFlashAttribute("class", "alert-info");
                redirectAttributes.addFlashAttribute("message",
                        create.size() + "人の生徒を" + year + "年度" + nen + "年" + kumi + "組に追加しました");
                return "redirect:/setting/class/list";
            } else {
                return "redirect:/setting/class/register";
            }
        } else {
            redirectAttributes.addFlashAttribute("hasMessage", true);
            redirectAttributes.addFlashAttribute("class", "alert-danger");
            redirectAttributes.addFlashAttribute("message", "生徒の重複があるため登録できませんでした");
            return "redirect:/setting/class/register";
        }
    }

    // path: /setting/class/new
    // クラス個別追加画面を表示
    @GetMapping("/class/new")
    public String newClass(Model model, @ModelAttribute("year") Long year, @ModelAttribute("nen") Long nen,
            @ModelAttribute("kumi") Long kumi) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        model.addAttribute("studentsYear", result);
        List<Student> studentsOption = studentsYearService.studentsOption(year, nen);
        model.addAttribute("students", studentsOption);
        return "setting/class/new";
    }

    // path: /setting/class/add
    // クラス個別追加画面から生徒1名追加
    @PostMapping("/class/add")
    public String addClass(RedirectAttributes redirectAttributes, Model model, @ModelAttribute("year") Long year,
            @ModelAttribute("nen") Long nen, @ModelAttribute("kumi") Long kumi, StudentYearForm studentYearForm,
            @AuthenticationPrincipal User user) {
        boolean isDuplicated = studentsYearService.isDuplicated(studentYearForm, year, nen, kumi);
        if (isDuplicated == false) {
            studentsYearService.add(user.getUserId(), studentYearForm, year, nen, kumi);
            redirectAttributes.addFlashAttribute("year", year);
            redirectAttributes.addFlashAttribute("nen", nen);
            redirectAttributes.addFlashAttribute("kumi", kumi);
            redirectAttributes.addFlashAttribute("hasMessage", true);
            redirectAttributes.addFlashAttribute("class", "alert-info");
            redirectAttributes.addFlashAttribute("message", "生徒を" + year + "年度" + nen + "年" + kumi + "組に追加しました");
            return "redirect:/setting/class/list";
        } else {
            redirectAttributes.addFlashAttribute("hasMessage", true);
            redirectAttributes.addFlashAttribute("class", "alert-danger");
            redirectAttributes.addFlashAttribute("message", "生徒の重複があるため追加できませんでした");
            return "redirect:/setting/class/new";
        }
    }

    // path: /setting/class/{studentYearId}
    // クラス編集画面を表示
    @GetMapping("/class/{id}")
    public String editClass(@PathVariable Long id, Model model) {
        StudentYear studentYear = studentsYearService.findById(id);
        model.addAttribute("studentYear", studentYear);
        if (studentYear.getImage() != null) {
            String image = studentsYearService.image(id);
            model.addAttribute("image", image);
        }
        boolean dataExists = studentsYearService.dataExists(id);
        model.addAttribute("dataExists", dataExists);
        return "setting/class/edit";
    }

    // path: /setting/class/{studentYearId}/delete
    // クラス編集画面から生徒を削除
    @PostMapping("/class/{id}/delete")
    public String deleteClass(RedirectAttributes redirectAttributes, Model model, @ModelAttribute("year") Long year,
            @ModelAttribute("nen") Long nen, @ModelAttribute("kumi") Long kumi, @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        boolean dataExists = studentsYearService.dataExists(id);
        if (dataExists == false) {
            studentsYearService.deleteClass(id);
            studentsYearService.sort(year, nen, kumi, user.getUserId());
            redirectAttributes.addFlashAttribute("year", year);
            redirectAttributes.addFlashAttribute("nen", nen);
            redirectAttributes.addFlashAttribute("kumi", kumi);
            redirectAttributes.addFlashAttribute("hasMessage", true);
            redirectAttributes.addFlashAttribute("class", "alert-info");
            redirectAttributes.addFlashAttribute("message", "生徒を" + year + "年度" + nen + "年" + kumi + "組から削除しました");
        }
        return "redirect:/setting/class/list";
    }

    // path: /setting/class/{studentYearId}/image/add
    // クラス編集画面から写真を追加
    @PostMapping("/class/{id}/image/add")
    public String upload(@PathVariable Long id, @AuthenticationPrincipal User user, StudentYearForm studentYearForm,
            BindingResult result, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile file)
            throws IOException {
        // ファイルタイプのバリデーション
        String contentType = studentYearForm.getImage().getContentType();
        if ((!contentType.startsWith("image/")) && (!(studentYearForm.getImage().isEmpty()))) {
            redirectAttributes.addFlashAttribute("hasMessage", true);
            redirectAttributes.addFlashAttribute("class", "alert-danger");
            redirectAttributes.addFlashAttribute("message", "登録に失敗しました。画像ファイルをアップロードしてください");
        }
        // その他のエラー
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("hasMessage", true);
            redirectAttributes.addFlashAttribute("class", "alert-danger");
            redirectAttributes.addFlashAttribute("message", "登録に失敗しました");
        }
        if ((contentType.startsWith("image/")) && (!(studentYearForm.getImage().isEmpty()))) {
            try {
                studentsYearService.addImage(id, user.getUserId(), studentYearForm);
                redirectAttributes.addFlashAttribute("hasMessage", true);
                redirectAttributes.addFlashAttribute("class", "alert-info");
                redirectAttributes.addFlashAttribute("message", "写真を登録しました");
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("hasMessage", true);
                redirectAttributes.addFlashAttribute("class", "alert-danger");
                redirectAttributes.addFlashAttribute("message", "登録に失敗しました。ファイルが読み込みできません" + " " + e);
            }
        }
        return "redirect:/setting/class/" + id;
    }

    // path: /setting/class/{studentYearId}/image/delete
    // クラス編集画面から写真を削除
    @GetMapping("/class/{id}/image/delete")
    public String deleteImage(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentsYearService.deleteImage(id);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "写真を削除しました");
        return "redirect:/setting/class/" + id;
    }

    // path: /setting/class/graduated
    // クラス管理から3年生クラスの生徒を卒業登録する
    @GetMapping("/class/graduated")
    public String graduated(Model model, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes,
            @ModelAttribute("year") Long year, @ModelAttribute("nen") Long nen, @ModelAttribute("kumi") Long kumi) {
        // 現在の「年度」
        LocalDate date = LocalDate.now();
        int nendo;
        if (date.getMonthValue() >= 4) {
            nendo = date.getYear();
        } else {
            nendo = date.getYear() - 1;
        }
        if (nen == 3 && year <= (long) nendo) {
            studentsService.graduated(user.getUserId(), year, nen, kumi);
            redirectAttributes.addFlashAttribute("year", year);
            redirectAttributes.addFlashAttribute("nen", nen);
            redirectAttributes.addFlashAttribute("kumi", kumi);
            redirectAttributes.addFlashAttribute("hasMessage", true);
            redirectAttributes.addFlashAttribute("class", "alert-info");
            redirectAttributes.addFlashAttribute("message", "卒業登録が完了しました");
        }
        return "redirect:/setting/class/list";
    }

    // path: /setting/schedule
    // スケジュール管理ページを表示
    @GetMapping("/schedule")
    public String schedule() {
        return "setting/schedule";
    }

    // path: /setting/schedule/year
    // 年度でスケジュール管理ページを表示
    @GetMapping("/schedule/year")
    public String schedule(Model model, @ModelAttribute("year") Long year) {
        ArrayList<ArrayList<Schedule>> yearSchedule = scheduleService.yearSchedule(year);
        model.addAttribute("schedule", yearSchedule);
        // 月登校日数（1年分）
        List<Integer> yearScheduleSize = scheduleService.yearScheduleSize(year);
        model.addAttribute("size", yearScheduleSize);
        // 年間合計登校日数
        int yearScheduleCount = scheduleService.yearScheduleCount(year);
        model.addAttribute("count", yearScheduleCount);
        return "setting/schedule";
    }

    // path: /setting/schedule/new
    // 初回スケジュール一括追加
    @PostMapping("/schedule/new")
    public String newSchedule(RedirectAttributes redirectAttributes, @ModelAttribute("year") Long year,
            @AuthenticationPrincipal User user) {
        scheduleService.newSchedule(year, user.getUserId());
        redirectAttributes.addFlashAttribute("year", year);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", year + "年度のスケジュールを登録しました");
        return "redirect:/setting/schedule/year";
    }

    // path: /setting/schedule/update
    // スケジュール更新
    @PostMapping("/schedule/update")
    public String updateSchedule(RedirectAttributes redirectAttributes, ScheduleForm scheduleForm,
            @AuthenticationPrincipal User user, Model model, @ModelAttribute("year") Long year) {
        scheduleService.update(user.getUserId(), scheduleForm);
        redirectAttributes.addFlashAttribute("year", year);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", year + "年度のスケジュールを更新しました");
        return "redirect:/setting/schedule/year";
    }

    // path: /setting/teachers
    // 教師管理ページを表示
    @GetMapping("/teachers")
    public String teachers(Model model) {
        List<User> teachers = usersService.findAll();
        model.addAttribute("teachers", teachers);
        return "setting/teachers";
    }

    // path: /setting/teachers/{userId}/update
    // 教師情報を更新
    @PostMapping("/teachers/{id}/update")
    public String updateTeacher(@PathVariable String id, @ModelAttribute @Validated UserEditForm form, BindingResult result,
            @AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("hasMessage", true);
            redirectAttributes.addFlashAttribute("class", "alert-danger");
            redirectAttributes.addFlashAttribute("message", "更新に失敗しました");
            User teacher = usersService.findById(id);
            redirectAttributes.addFlashAttribute("teacher", teacher);
            return "redirect:/setting/teachers";
        }
        usersService.updateByAdmin(id, form, user.getUserId());
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "教師を更新しました");
        return "redirect:/setting/teachers";
    }

    // path: /setting/teachers/{userId}/delete
    // 教師を削除
    @GetMapping("/teachers/{id}/delete")
    public String deleteTeacher(@PathVariable String id, RedirectAttributes redirectAttributes) {
        usersService.delete(id);
        redirectAttributes.addFlashAttribute("hasMessage", true);
        redirectAttributes.addFlashAttribute("class", "alert-info");
        redirectAttributes.addFlashAttribute("message", "教師を削除しました");
        return "redirect:/setting/teachers";
    }

}
