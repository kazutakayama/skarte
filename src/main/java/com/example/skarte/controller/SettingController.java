package com.example.skarte.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.skarte.bean.StudentsCsv;
import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Karte;
import com.example.skarte.entity.Schedule;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.entity.User;
import com.example.skarte.form.ScheduleForm;
import com.example.skarte.form.StudentForm;
import com.example.skarte.form.StudentYearForm;
import com.example.skarte.repository.UserRepository;
import com.example.skarte.service.AttendanceService;
import com.example.skarte.service.GradeService;
import com.example.skarte.service.ScheduleService;
import com.example.skarte.service.StudentsService;
import com.example.skarte.service.StudentsYearService;
import com.example.skarte.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

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

    private final AttendanceService attendanceService;
    private final GradeService gradeService;

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
    public String newStudent() {
        return "setting/students/new";
    }

    // path: /setting/students/add
    // 画面で入力された生徒情報を取得して、dbに登録をする
    @PostMapping("/students/add")
    public String addStudent(StudentForm form, @AuthenticationPrincipal User user) {
        studentsService.add(user.getUserId(), form);
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

        // 管理用
        List<Attendance> attendance = attendanceService.findAllByStudentId(id);
        model.addAttribute("attendance", attendance);
        List<Grade> grade = gradeService.findAllByStudentId(id);
        model.addAttribute("grade", grade);

        return "setting/students/details";
    }

    // path: /setting/students/{id}/edit
    // 生徒情報編集画面を表示
    @GetMapping("/students/{id}/edit")
    public String edit(@PathVariable String id, Model model) {
        Student student = studentsService.findById(id);
        model.addAttribute("student", student);
        boolean dataExists = studentsService.dataExists(id);
        model.addAttribute("dataExists", dataExists);
        return "setting/students/edit";
    }

    // path: /setting/students/{id}/update
    // 生徒情報編集画面から生徒情報を更新する
    @PostMapping("/students/{id}/update")
    public String updateStudent(@PathVariable String id, StudentForm form, @AuthenticationPrincipal User user) {
        studentsService.update(id, form, user.getUserId());
        return "redirect:/setting/students/" + id;
    }

//    // path: /setting/students/download.csv
//    // 生徒をcsvでダウンロードする
//    @GetMapping(value = "/students/download.csv", params = "download_file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
//            + "; charset=UTF-8; Content-Disposition: attachment")
//    @ResponseBody
//    public Object download() throws JsonProcessingException {
//        return studentsService.download();
//    }

    // path: /setting/students/download.csv
    // 名前、年度で絞り込み後、生徒をcsvでダウンロードする
    @GetMapping(value = "/students/download.csv", params = "download_file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
            + "; charset=UTF-8; Content-Disposition: attachment")
    @ResponseBody
    public Object download(@RequestParam("name") String name, @RequestParam("year") String year)
            throws JsonProcessingException {
        return studentsService.download(name, year);
    }

//    // path: /setting/students/upload
//    // 生徒をcsvでアップロードする ★一旦完成版
//    @PostMapping(value = "/students/upload", params = "upload_file")
//    public String uploadFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user) throws IOException {
//        try (BufferedReader br = new BufferedReader(
//                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
//            studentsService.uploadCsv(user.getUserId(), br);
//        } catch (IOException e) {
//            throw new RuntimeException("ファイルが読み込めません", e);
//        }
//        return "redirect:/setting/students";
//    }

    // path: /setting/students/upload
    // 生徒をcsvでアップロードする★★★お試し！
    @PostMapping(value = "/students/upload", params = "upload_file")
    public String uploadFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user)
            throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            studentsService.upload(user.getUserId(), br);
        } catch (IOException e) {
            throw new RuntimeException("ファイルが読み込めません", e);
        }
        return "redirect:/setting/students";
    }

//    // path: /setting/students/upload
//    // 生徒をcsvでアップロードする
//    @PostMapping(value = "/students/upload", params = "upload_file")
//    public String uploadFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user)
//            throws IOException {
//        try (BufferedReader br = new BufferedReader(
//                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
//            String line;
//            // ヘッダーレコードをとばすためにあらかじめ１行だけ読み取っておく
//            line = br.readLine();
//            // 行がNULL（CSVの値がなくなる）になるまで処理を繰り返す
//            while ((line = br.readLine()) != null) {
//                // 負の数字を引数に指定し、中身が空でも、全ての要素を取得
//                String[] split = line.split(",", -1);
//                Student student = Student.builder().studentId(split[0]).lastName(split[1]).firstName(split[2])
//                        .lastNameKana(split[3]).firstNameKana(split[4]).birth(Date.valueOf(split[5]))
//                        .gender((int) Integer.parseInt(split[6])).family1(split[7]).family2(split[8]).tel1(split[9])
//                        .tel2(split[10]).tel3(split[11]).tel4(split[12]).postalCode(split[13]).adress(split[14])
//                        .memo(split[15]).build();
//                studentsService.upload(user.getUserId(), student);
//            }
//            br.close();
//        } catch (IOException e) {
//            throw new RuntimeException("ファイルが読み込めません", e);
//        }
//        return "redirect:/setting/students";
//    }

    // path: /setting/students/{id}/delete
    // 設定/生徒を削除（物理削除）
    @GetMapping("/students/{id}/delete")
    public String delete(@PathVariable String id) {
        studentsService.delete(id);
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
        studentsYearService.create(user.getUserId(), studentYearForm, year, nen, kumi);
        studentsYearService.sort(year, nen, kumi, user.getUserId());
        redirectAttributes.addFlashAttribute("year", year);
        redirectAttributes.addFlashAttribute("nen", nen);
        redirectAttributes.addFlashAttribute("kumi", kumi);
        return "redirect:/setting/class/list";
    }

    // path: /setting/class/new
    // クラス個別登録画面を表示
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
    // クラス個別登録画面から生徒1名追加
    @PostMapping("/class/add")
    public String addClass(RedirectAttributes redirectAttributes, Model model, @ModelAttribute("year") Long year,
            @ModelAttribute("nen") Long nen, @ModelAttribute("kumi") Long kumi, StudentYearForm studentYearForm,
            @AuthenticationPrincipal User user) {
        studentsYearService.add(user.getUserId(), studentYearForm, year, nen, kumi);
        redirectAttributes.addFlashAttribute("year", year);
        redirectAttributes.addFlashAttribute("nen", nen);
        redirectAttributes.addFlashAttribute("kumi", kumi);
        return "redirect:/setting/class/list";
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
        }
        return "redirect:/setting/class/list";
    }

    // path: /setting/class/{studentYearId}/upload
    // クラス編集画面から写真を登録
    @PostMapping("/class/{id}/upload")
    public String upload(@PathVariable Long id, @AuthenticationPrincipal User user, StudentYearForm studentYearForm)
            throws IOException {
        if (!(studentYearForm.getImage().isEmpty())) {
            studentsYearService.upload(id, user.getUserId(), studentYearForm);
        }
        return "redirect:/setting/class/" + id;
    }

    // path: /setting/class/{studentYearId}/deleteImage
    // クラス編集画面から写真を削除
    @GetMapping("/class/{id}/deleteImage")
    public String deleteImage(@PathVariable Long id) {
        studentsYearService.deleteImage(id);
        return "redirect:/setting/class/" + id;
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
        List<Integer> yearScheduleSize = scheduleService.yearScheduleSize(year);
        model.addAttribute("size", yearScheduleSize);
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
        return "redirect:/setting/schedule/year";
    }

    // path: /setting/schedule/update
    // スケジュール更新
    @PostMapping("/schedule/update")
    public String updateSchedule(RedirectAttributes redirectAttributes, ScheduleForm scheduleForm,
            @AuthenticationPrincipal User user, Model model, @ModelAttribute("year") Long year) {
        scheduleService.update(user.getUserId(), scheduleForm);
        redirectAttributes.addFlashAttribute("year", year);
        return "redirect:/setting/schedule/year";
    }

    // path: /setting/teachers
    // 教師一覧ページを表示
    @GetMapping("/teachers")
    public String teachers(Model model) {
        List<User> teachers = usersService.findAll();
        model.addAttribute("teachers", teachers);
        return "setting/teachers";
    }

    //
    // 以下管理用

    // path: /setting/students/{attendanceId}/deleteAttendance
    // 出欠を削除
    @GetMapping("/students/{id}/deleteAttendance")
    public String deleteAttendance(@PathVariable Long id) {
        Attendance attendance = attendanceService.findById(id);
        attendanceService.deleteAttendance(id);
        return "redirect:/setting/students/" + attendance.getStudentId();
    }

    // path: /setting/students/{gradeId}/deleteGrade
    // 成績を削除
    @GetMapping("/students/{id}/deleteGrade")
    public String deleteGrade(@PathVariable Long id) {
        Grade grade = gradeService.findById(id);
        gradeService.deleteGrade(id);
        return "redirect:/setting/students/" + grade.getStudentId();
    }

}
