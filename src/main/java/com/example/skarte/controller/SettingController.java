package com.example.skarte.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.skarte.bean.StudentsCsv;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.service.StudentsService;
import com.example.skarte.service.StudentsYearService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/setting")
//コンストラタ生成アノテーション
@RequiredArgsConstructor
public class SettingController {

    // コンストラクタインジェクション
    private final StudentsService studentsService;
    private final StudentsYearService studentsYearService;

//    @Autowired
//    private StudentsService studentsService;

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
        return "setting/students";
    }

    // path: /setting/students/newstudent
    // 生徒新規登録ページを表示
    @GetMapping("students/newstudent")
    public String newStudent() {
        return "setting/newstudent";
    }

    // path: /setting/students/addstudent
    // 画面で入力された生徒情報を取得して、dbに登録をする
    @PostMapping("/students/addstudent")
    public String addStudent(@ModelAttribute Student student) {
        studentsService.addStudent(student);
//        studentsService.save(student);
        return "setting/students";
    }

    // path: /setting/students/{id}/details
    // 生徒詳細画面を表示
    @GetMapping("/students/{id}/details")
    public String details(@PathVariable Long id, Model model) {
        Student students = studentsService.findById(id);
        model.addAttribute("students", students);
        List<StudentYear> studentsYear = studentsYearService.findAll();
        model.addAttribute("studentsYear", studentsYear);
        return "setting/details";
    }

    // path: /setting/students/{id}/editstudent
    // 生徒情報編集画面を表示
    @GetMapping("/students/{id}/editstudent")
    public String editStudent(@PathVariable Long id, Model model) {
        Student students = studentsService.findById(id);
        model.addAttribute("students", students);
        return "setting/editstudent";
    }
        
     // path: /setting/students/{id}/updatestudent
        // 生徒情報編集画面から投稿を更新する
    @PostMapping("/students/{id}/updatestudent")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student) {
        Student result = studentsService.updateStudent(id, student);
        return "redirect:/setting/students/" + result.getStudentId() + "/details";
    }


    // path: /setting/students/download.csv
    // 生徒をcsvでダウンロードする
    @GetMapping(value = "/students/download.csv", params = "download_file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
            + "; charset=UTF-8; Content-Disposition: attachment")
    @ResponseBody
    public Object download() throws JsonProcessingException {
        // DBから取得
        List<Student> students = studentsService.findAll();
        // CSVファイル用のDTOに詰め直す
        List<StudentsCsv> csvs = students.stream()
                .map(e -> new StudentsCsv(e.getStudentId(), e.getLastName(), e.getFirstName(), e.getLastNameKana(),
                        e.getFirstNameKana(), e.getFamily1(), e.getTel1(), e.getPostalCode(), e.getAdress(),
                        e.getMemo()))
                .collect(Collectors.toList());
        // ファイルをダウンロードさせる
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(StudentsCsv.class).withHeader();
        return mapper.writer(schema).writeValueAsString(csvs);
    }

    // path: /setting/students/upload.csv
    // 生徒をcsvでアップロードする
    @PostMapping(value = "/students/upload.csv", params = "upload_file")
    public String uploadFile(@RequestParam("file") MultipartFile uploadFile) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(uploadFile.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] split = line.split(",");
                final Student student = Student.builder().studentId((long) Integer.parseInt(split[0]))
                        .lastName(split[1]).firstName(split[2]).lastNameKana(split[3]).firstNameKana(split[4])
                        .family1(split[5]).tel1((long) Integer.parseInt(split[6]))
                        .postalCode((long) Integer.parseInt(split[7])).adress(split[8]).memo(split[9]).build();
                studentsService.insertStudent(student);
            }
        } catch (IOException e) {
            throw new RuntimeException("ファイルが読み込めません", e);
        }
        return "redirect:/setting/students";
    }

    // path: /setting/students/{id}/deletestudent
    // 生徒を削除（論理削除）
    @GetMapping("/students/{id}/deletestudent")
    public String delete(@PathVariable Long id, @ModelAttribute Student student) {
        studentsService.deleteStudent(id, student);
        return "redirect:/setting/students";
    }

    // path: /setting/students/{id}/delete
    // 設定/生徒を削除（物理削除）
//    @GetMapping("/students/{id}/delete")
//    public String delete(@PathVariable Long id, @ModelAttribute Student student) {
//        studentsService.delete(id);
//        return "redirect:/setting/students";
//    }

    // path: /setting/students/addclass
    // 画面で入力されたクラス情報を取得して、dbに登録をする
    @PostMapping("/students/addclass")
    public String addClass(@ModelAttribute StudentYear studentYear) {
        studentsYearService.addClass(studentYear);
        return "redirect:/setting/students";
    }

    // path: /setting/students/{id}/editclass
    // クラス編集画面を表示
    @GetMapping("/students/{id}/editclass")
    public String editClass(@PathVariable Long id, Model model) {
        StudentYear studentsYear = studentsYearService.findById(id);
        model.addAttribute("studentsYear", studentsYear);
//        List<StudentYear> studentsYear = studentsYearService.findAll();
//        model.addAttribute("studentsYear", studentsYear);
        return "setting/editclass";
    }

    // path: /setting/students/{id}/updateclass
    // クラス情報を更新する
    @PostMapping("/students/{id}/updateclass")
    public String updateClass(@PathVariable Long id, @ModelAttribute StudentYear studentYear) {
        StudentYear result = studentsYearService.updateClass(id, studentYear);
        return "redirect:/setting/students/" + result.getStudentId() + "/details";
    }

    // path: /setting/students/{id}/deletestudent
    // クラスを削除（論理削除）
    @GetMapping("/students/{id}/deleteclass")
    public String deleteClass(@PathVariable Long id, @ModelAttribute StudentYear studentYear) {
        studentsYearService.deleteClass(id, studentYear);
        return "redirect:/setting/students";
    }

    // path: /setting/class
    // クラス管理ページを表示
    @GetMapping("/class")
    public String Class(Model model) {
        List<Student> students = studentsService.findAll();
        model.addAttribute("students", students);
        List<StudentYear> studentsYear = studentsYearService.findAll();
        model.addAttribute("studentsYear", studentsYear);
        return "setting/class";
    }

}
