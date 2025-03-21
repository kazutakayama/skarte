//package com.example.skarte.controller;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.sql.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.example.skarte.bean.StudentsCsv;
//import com.example.skarte.entity.Student;
//import com.example.skarte.entity.StudentYear;
//import com.example.skarte.entity.User;
//import com.example.skarte.form.StudentForm;
//import com.example.skarte.service.StudentsService;
//import com.example.skarte.service.StudentsYearService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.dataformat.csv.CsvMapper;
//import com.fasterxml.jackson.dataformat.csv.CsvSchema;
//
//@Controller
//@RequestMapping("/setting")
//public class SettingController {
//
//    @Autowired
//    private StudentsService studentsService;
//    private StudentsYearService studentsYearService;
//
//    // path: /setting
//    @GetMapping("")
//    public String index() {
//        return "setting/index";
//    }
//
//    // path: /setting/students
//    // 生徒管理ページを表示
//    @GetMapping("/students")
//    public String students(Model model) {
//        List<Student> students = studentsService.findAll();
//        model.addAttribute("students", students);
//        return "setting/students";
//    }
//
//    // path: /setting/students/newstudent
//    // 生徒新規登録ページを表示
//    @GetMapping("students/newstudent")
//    public String newStudent() {
//        return "setting/newstudent";
//    }
//
//    // path: /setting/students/addstudent
//    // 画面で入力された生徒情報を取得して、dbに登録をする
//    @PostMapping("/students/addstudent")
////    public String addStudent(@ModelAttribute Student student, @AuthenticationPrincipal User user) {
//    public String addStudent(@Validated @ModelAttribute StudentForm form, @AuthenticationPrincipal User user,
//            BindingResult bindingResult, Model model) {
//        // Student student = new student();
//        studentsService.addStudent(user.getUserId(), form);
//        return "redirect:/setting/students";
//    }
//
//    // path: /setting/students/{id}
//    // 生徒詳細画面を表示
//    @GetMapping("/students/{id}")
//    public String details(@PathVariable Long id, Model model) {
//        Student student = studentsService.findById(id);
//        model.addAttribute("students", student);
//        List<StudentYear> studentsYear = studentsYearService.findAllByStudentId(id);
//        model.addAttribute("studentsYear", studentsYear);
//
//        return "setting/details";
//    }
//
//    // path: /setting/students/{id}/editstudent
//    // 生徒情報編集画面を表示
//    @GetMapping("/students/{id}/editstudent")
//    public String editStudent(@PathVariable Long id, Model model) {
//        Student student = studentsService.findById(id);
//        model.addAttribute("students", student);
//        return "setting/editstudent";
//    }
//
//    // path: /setting/students/{id}/updatestudent
//    // 生徒情報編集画面から生徒情報を更新する
//    @PostMapping("/students/{id}/updatestudent")
////    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student, @AuthenticationPrincipal User user) {
//    public String updateStudent(@PathVariable Long id, @Validated @ModelAttribute StudentForm form,
//            @AuthenticationPrincipal User user) {
//        form.setUpdatedBy(user.getUserId());
//        Student result = studentsService.updateStudent(id, form);
//        return "redirect:/setting/students/" + result.getStudentId();
//    }
//
//    // path: /setting/students/download.csv
//    // 生徒をcsvでダウンロードする
//    @GetMapping(value = "/students/download.csv", params = "download_file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
//            + "; charset=UTF-8; Content-Disposition: attachment")
//    @ResponseBody
//    public Object download() throws JsonProcessingException {
//        // DBから取得
//        List<Student> students = studentsService.findAll();
//        // CSVファイル用のDTOに詰め直す
//        List<StudentsCsv> csvs = students.stream()
//                .map(e -> new StudentsCsv(e.getStudentId(), e.getLastName(), e.getFirstName(), e.getLastNameKana(),
//                        e.getFirstNameKana(), e.getBirth(), e.getGender(), e.getFamily1(), e.getFamily2(), e.getTel1(),
//                        e.getTel2(), e.getTel3(), e.getTel4(), e.getPostalCode(), e.getAdress(), e.getMemo()))
//                .collect(Collectors.toList());
//        // ファイルをダウンロードさせる
//        CsvMapper mapper = new CsvMapper();
//        CsvSchema schema = mapper.schemaFor(StudentsCsv.class).withHeader();
//        return mapper.writer(schema).writeValueAsString(csvs);
//    }
//
////    // path: /setting/students/upload.csv
////    // 生徒をcsvでアップロードする
////    @PostMapping(value = "/students/upload.csv", params = "upload_file")
////    public String uploadFile(@RequestParam("file") MultipartFile uploadFile, @AuthenticationPrincipal User user) {
////        try (BufferedReader br = new BufferedReader(
////                new InputStreamReader(uploadFile.getInputStream(), StandardCharsets.UTF_8))) {
////            String line;
////            while ((line = br.readLine()) != null) {
////                final String[] split = line.split(",");
////                final Student student = Student.builder().studentId((long) Integer.parseInt(split[0]))
////                        .lastName(split[1]).firstName(split[2]).lastNameKana(split[3]).firstNameKana(split[4])
////                        .birth(Date.valueOf(split[5])).gender((int) Integer.parseInt(split[6])).family1(split[7])
////                        .family2(split[8]).tel1((long) Integer.parseInt(split[9]))
////                        .tel2((long) Integer.parseInt(split[10])).tel3((long) Integer.parseInt(split[11]))
////                        .tel4((long) Integer.parseInt(split[12])).postalCode((long) Integer.parseInt(split[13]))
////                        .adress(split[14]).memo(split[15]).build();
////                studentsService.addStudentByCSV(user.getUserId(), student);
////            }
////        } catch (IOException e) {
////            throw new RuntimeException("ファイルが読み込めません", e);
////        }
////        return "redirect:/setting/students";
////    }
//
////    // path: /setting/students/{id}/deletestudent
////    // 生徒を削除（論理削除）
////    @GetMapping("/students/{id}/deletestudent")
////    public String delete(@PathVariable Long id, @ModelAttribute Student student, @AuthenticationPrincipal User user) {
////        student.setUpdatedBy(user.getUserId());
////        studentsService.deleteStudent(id, student);
////        return "redirect:/setting/students";
////    }
//
//    // path: /setting/students/{id}/delete
//    // 設定/生徒を削除（物理削除）
////    @GetMapping("/students/{id}/delete")
////    public String delete(@PathVariable Long id, @ModelAttribute Student student) {
////        studentsService.delete(id);
////        return "redirect:/setting/students";
////    }
//
//    // path: /setting/students/addclass
//    // 画面で入力されたクラス情報を取得して、dbに登録をする
//    @PostMapping("/students/addclass")
//    public String addClass(@ModelAttribute StudentYear studentYear, @AuthenticationPrincipal User user) {
//        studentsYearService.addClass(user.getUserId(), studentYear);
//        return "redirect:/setting/students";
////        return "redirect:/setting/students/" + result.getStudentId();
//    }
//
//    // path: /setting/students/{id}/editclass
//    // クラス編集画面を表示
//    @GetMapping("/students/{id}/editclass")
//    public String editClass(@PathVariable Long id, Model model) {
//        StudentYear studentYear = studentsYearService.findById(id);
//        model.addAttribute("studentsYear", studentYear);
////        List<StudentYear> studentsYear = studentsYearService.findAll();
////        model.addAttribute("studentsYear", studentsYear);
//        return "setting/editclass";
//    }
//
//    // path: /setting/students/{id}/updateclass
//    // クラス情報を更新する
//    @PostMapping("/students/{id}/updateclass")
//    public String updateClass(@PathVariable Long id, @ModelAttribute StudentYear studentYear,
//            @AuthenticationPrincipal User user) {
//        studentYear.setUpdatedBy(user.getUserId());
//        StudentYear result = studentsYearService.updateClass(id, studentYear);
//        return "redirect:/setting/students";
////        return "redirect:/setting/students/" + result.getStudentId() + "/details";
//    }
//
////    // path: /setting/students/{id}/deletestudent
////    // クラスを削除（論理削除）
////    @GetMapping("/students/{id}/deleteclass")
////    public String deleteClass(@PathVariable Long id, @ModelAttribute StudentYear studentYear, @AuthenticationPrincipal User user) {
////        studentYear.setUpdatedBy(user.getUserId());
////        studentsYearService.deleteClass(id, studentYear);
////        return "redirect:/setting/students";
////    }
//
//    // path: /setting/class
//    // クラス管理ページを表示
//    @GetMapping("/class")
//    public String Class(Model model) {
//        List<Student> students = studentsService.findAll();
//        model.addAttribute("students", students);
//        List<StudentYear> studentsYear = studentsYearService.findAll();
//        model.addAttribute("studentsYear", studentsYear);
//        return "setting/class";
//    }
//
//}

package com.example.skarte.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.multipart.MultipartFile;

import com.example.skarte.bean.StudentsCsv;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.entity.User;
import com.example.skarte.form.StudentForm;
import com.example.skarte.repository.UserRepository;
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
//    private StudentsYearService studentsYearService;

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

    // path: /setting/students/new
    // 生徒新規登録ページを表示
    @GetMapping("students/new")
    public String newStudent() {
        return "setting/new";
    }

    // path: /setting/students/addstudent
    // 画面で入力された生徒情報を取得して、dbに登録をする
    @PostMapping("/students/addStudent")
    public String addStudent(@Validated @ModelAttribute StudentForm form, @AuthenticationPrincipal User user,
            BindingResult bindingResult, Model model) {
        studentsService.addStudent(user.getUserId(), form);
        return "redirect:/setting/students";
    }
    // ★新規登録後、その登録した生徒の詳細画面に遷移させたい

    // path: /setting/students/{id}
    // 生徒詳細画面を表示
    @GetMapping("/students/{id}")
    public String details(@PathVariable Long id, Model model) {
        Student student = studentsService.findById(id);
        model.addAttribute("students", student);
        List<StudentYear> studentsYear = studentsYearService.findAllByStudentId(id);
        model.addAttribute("studentsYear", studentsYear);
        return "setting/details";
    }

//    // path: /setting/students/{id}/editStudent
//    // 生徒情報編集画面を表示
//    @GetMapping("/students/{id}/editStudent")
//    public String editStudent(@PathVariable Long id, Model model) {
//        Student student = studentsService.findById(id);
//        model.addAttribute("students", student);
//        return "setting/editStudent";
//    }

    // path: /setting/students/{id}/editStudent
    // 生徒情報編集画面を表示
    @GetMapping("/students/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Student student = studentsService.findById(id);
        model.addAttribute("students", student);
        List<StudentYear> studentsYear = studentsYearService.findAllByStudentId(id);
        model.addAttribute("studentsYear", studentsYear);
//        StudentYear studentYear = studentsYearService.findById(id);
//        model.addAttribute("studentsYear", studentYear);
        return "setting/edit";
    }

    // path: /setting/students/{id}/updatestudent
    // 生徒情報編集画面から生徒情報を更新する
    @PostMapping("/students/{id}/updateStudent")
    public String updateStudent(@PathVariable Long id, @Validated @ModelAttribute StudentForm form,
            @AuthenticationPrincipal User user) {
        form.setUpdatedBy(user.getUserId());
        Student result = studentsService.updateStudent(id, form);
        return "redirect:/setting/students/" + result.getStudentId();
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
                        e.getFirstNameKana(), e.getBirth(), e.getGender(), e.getFamily1(), e.getFamily2(), e.getTel1(),
                        e.getTel2(), e.getTel3(), e.getTel4(), e.getPostalCode(), e.getAdress(), e.getMemo()))
                .collect(Collectors.toList());
        // ファイルをダウンロードさせる
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(StudentsCsv.class).withHeader();
        return mapper.writer(schema).writeValueAsString(csvs);
    }

    // path: /setting/students/upload.csv
    // 生徒をcsvでアップロードする
    @PostMapping(value = "/students/upload.csv", params = "upload_file")
    public String uploadFile(@RequestParam("file") MultipartFile uploadFile, @AuthenticationPrincipal User user) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(uploadFile.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] split = line.split(",");
                final Student student = Student.builder().studentId((long) Integer.parseInt(split[0]))
                        .lastName(split[1]).firstName(split[2]).lastNameKana(split[3]).firstNameKana(split[4])
                        .birth(Date.valueOf(split[5])).gender((int) Integer.parseInt(split[6])).family1(split[7])
                        .family2(split[8]).tel1((long) Integer.parseInt(split[9]))
                        .tel2((long) Integer.parseInt(split[10])).tel3((long) Integer.parseInt(split[11]))
                        .tel4((long) Integer.parseInt(split[12])).postalCode((long) Integer.parseInt(split[13]))
                        .adress(split[14]).memo(split[15]).build();
                studentsService.addStudentByCSV(user.getUserId(), student);
            }
        } catch (IOException e) {
            throw new RuntimeException("ファイルが読み込めません", e);
        }
        return "redirect:/setting/students";
    }

//    // path: /setting/students/{id}/deletestudent
//    // 生徒を削除（論理削除）
//    @GetMapping("/students/{id}/deletestudent")
//    public String delete(@PathVariable Long id, @ModelAttribute Student student, @AuthenticationPrincipal User user) {
//        student.setUpdatedBy(user.getUserId());
//        studentsService.deleteStudent(id, student);
//        return "redirect:/setting/students";
//    }

    // path: /setting/students/{id}/deleteStudent
    // 設定/生徒を削除（物理削除）
    @GetMapping("/students/{id}/deleteStudent")
    public String delete(@PathVariable Long id, @ModelAttribute Student student) {
        studentsService.deleteStudent(id);
        return "redirect:/setting/students";
    }

    // path: /setting/students/addclass
    // 画面で入力されたクラス情報を取得して、dbに登録をする
    @PostMapping("/students/addclass")
    public String addClass(@ModelAttribute StudentYear studentYear, @AuthenticationPrincipal User user) {
        studentsYearService.addClass(user.getUserId(), studentYear);
        return "redirect:/setting/students";
    }
    // ★クラス登録後、その生徒の詳細画面にとまどまるようにしたい

    // path: /setting/students/{id}/editClass
    // クラス編集画面を表示
    @GetMapping("/students/{id}/editClass")
    public String editClass(@PathVariable Long id, Model model) {
        StudentYear studentYear = studentsYearService.findById(id);
        model.addAttribute("studentsYear", studentYear);
        return "setting/editClass";
    }

    // path: /setting/students/{id}/updateclass
    // クラス情報を更新する
    @PostMapping("/students/{id}/updateclass")
    public String updateClass(@PathVariable Long id, @ModelAttribute StudentYear studentYear,
            @AuthenticationPrincipal User user) {
        studentYear.setUpdatedBy(user.getUserId());
        StudentYear result = studentsYearService.updateClass(id, studentYear);
        return "redirect:/setting/students";
    }
    // ★クラス編集後、その生徒の詳細画面へ遷移したい

    // path: /setting/students/{id}/deleteClass
    // 設定/クラスを削除（物理削除）
    @GetMapping("/students/{id}/deleteClass")
    public String deleteClass(@PathVariable Long id, @ModelAttribute Student student) {
        studentsYearService.deleteClass(id);
        return "redirect:/setting/students";
    }
    // ★クラス削除後、その生徒の詳細画面へ遷移したい

//    // path: /setting/students/{id}/deletestudent
//    // クラスを削除（論理削除）
//    @GetMapping("/students/{id}/deleteclass")
//    public String deleteClass(@PathVariable Long id, @ModelAttribute StudentYear studentYear, @AuthenticationPrincipal User user) {
//        studentYear.setUpdatedBy(user.getUserId());
//        studentsYearService.deleteClass(id, studentYear);
//        return "redirect:/setting/students";
//    }

//    // path: /setting/class
//    // クラス管理ページを表示
//    @GetMapping("/class")
//    public String Class(Model model) {
//        List<Student> students = studentsService.findAll();
//        model.addAttribute("students", students);
//        List<StudentYear> studentsYear = studentsYearService.findAll();
//        model.addAttribute("studentsYear", studentsYear);
//        return "setting/class";
//    }

}
