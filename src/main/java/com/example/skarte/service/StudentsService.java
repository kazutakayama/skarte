package com.example.skarte.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
//import java.util.Date;
import java.sql.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.skarte.bean.StudentsCsv;
import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.form.StudentForm;
import com.example.skarte.repository.AttendanceRepository;
import com.example.skarte.repository.GradeRepository;
import com.example.skarte.repository.StudentRepository;
import com.example.skarte.repository.StudentYearRepository;
import com.example.skarte.specification.StudentSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
//import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentsService {

    private final StudentsYearService studentsYearService;

    private final StudentRepository studentRepository;
    private final StudentYearRepository studentYearRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;
    private final StudentSpecification studentSpecification;

    // 生徒全取得
    public List<Student> findAll() {
        return studentRepository.findByOrderByUpdatedAtDesc();
    }

    // 生徒１件取得
    public Student findById(String id) {
        return studentRepository.findById(id).orElseThrow();
    }

    // 生徒検索（生徒名、年度）
    public List<Student> search(String name, String year) {
        List<Student> result;
        if ("".equals(name)) {
            // すべての生徒
            if ("0".equals(year)) {
                result = studentRepository.findAll(Sort.by(Sort.Direction.ASC, "studentId"));
                // 年度検索
            } else {
                result = studentRepository.findAll(Specification.where(studentSpecification.year(year)),
                        Sort.by(Sort.Direction.ASC, "studentId"));
            }
        } else {
            // 名前検索
            if ("0".equals(year)) {
                result = studentRepository.findAll(Specification.where(studentSpecification.search(name)),
                        Sort.by(Sort.Direction.ASC, "studentId"));
                // 年度・名前検索
            } else {
                result = studentRepository.findAll(
                        Specification.where(studentSpecification.search(name)).and(studentSpecification.year(year)),
                        Sort.by(Sort.Direction.ASC, "studentId"));
            }
        }
        return result;
    }

    // クラス検索をしたあと、Studentのリストを取得
    public List<Student> classStudents(Long year, Long nen, Long kumi) {
        List<StudentYear> result = studentsYearService.search(year, nen, kumi);
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            Student student = studentRepository.findById(result.get(i).getStudentId()).orElseThrow();
            students.add(student);
        }
        return students;
    }

    // 生徒追加
    public void add(String userId, StudentForm form) {
        Student student = new Student();
        student.setStudentId(form.getStudentId());
        student.setLastName(form.getLastName());
        student.setFirstName(form.getFirstName());
        student.setLastNameKana(form.getLastNameKana());
        student.setFirstNameKana(form.getFirstNameKana());
        student.setBirth(form.getBirth());
        student.setGender(form.getGender());
        student.setFamily1(form.getFamily1());
        student.setFamily2(form.getFamily2());
        student.setTel1(form.getTel1());
        student.setTel2(form.getTel2());
        student.setTel3(form.getTel3());
        student.setTel4(form.getTel4());
        student.setPostalCode(form.getPostalCode());
        student.setAdress(form.getAdress());
        student.setMemo(form.getMemo());
        student.setCreatedBy(userId);
        student.setUpdatedBy(userId);
        studentRepository.save(student);
    }

    // 生徒更新
    public void update(String studentId, StudentForm form, String userId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        student.setLastName(form.getLastName());
        student.setFirstName(form.getFirstName());
        student.setLastNameKana(form.getLastNameKana());
        student.setFirstNameKana(form.getFirstNameKana());
        student.setBirth(form.getBirth());
        student.setGender(form.getGender());
        student.setFamily1(form.getFamily1());
        student.setFamily2(form.getFamily2());
        student.setTel1(form.getTel1());
        student.setTel2(form.getTel2());
        student.setTel3(form.getTel3());
        student.setTel4(form.getTel4());
        student.setPostalCode(form.getPostalCode());
        student.setAdress(form.getAdress());
        student.setMemo(form.getMemo());
        student.setTransferred(form.isTransferred());
        student.setUpdatedBy(userId);
        studentRepository.save(student);
    }

//    // 全生徒CSVダウンロード
//    public Object download() throws JsonProcessingException {
//        // DBから取得
//        List<Student> students = findAll();
//        // CSVファイル用のDTOに詰め直す
//        List<StudentsCsv> csvs = students.stream()
//                .map(e -> new StudentsCsv(e.getStudentId(), e.getLastName(), e.getFirstName(), e.getLastNameKana(),
//                        e.getFirstNameKana(), e.getBirth(), e.getGender(), e.getFamily1(), e.getFamily2(), e.getTel1(),
//                        e.getTel2(), e.getTel3(), e.getTel4(), e.getPostalCode(), e.getAdress(), e.getMemo()))
//                .collect(Collectors.toList());
//        // ファイルをダウンロード
//        CsvMapper mapper = new CsvMapper();
//        // 日付をフォーマット
//        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
//        CsvSchema schema = mapper.schemaFor(StudentsCsv.class).withHeader();
//        return mapper.writer(schema).writeValueAsString(csvs);
//    }

    // /setting クラスで生徒を絞り込んでCSVダウンロード
    public Object download(String name, String year) throws JsonProcessingException {
        List<Student> students = search(name, year);
        List<StudentsCsv> csvs = students.stream()
                .map(e -> new StudentsCsv(e.getStudentId(), e.getLastName(), e.getFirstName(), e.getLastNameKana(),
                        e.getFirstNameKana(), e.getBirth(), e.getGender(), e.getFamily1(), e.getFamily2(), e.getTel1(),
                        e.getTel2(), e.getTel3(), e.getTel4(), e.getPostalCode(), e.getAdress(), e.getMemo()))
                .collect(Collectors.toList());
        // ファイルをダウンロード
        CsvMapper mapper = new CsvMapper();
        // 日付をフォーマット
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        CsvSchema schema = mapper.schemaFor(StudentsCsv.class).withHeader();
        return mapper.writer(schema).writeValueAsString(csvs);
    }

    // /students クラスで生徒を絞り込んでCSVダウンロード
    public Object downloadClass(Long year, Long nen, Long kumi) throws JsonProcessingException {
        List<Student> students = classStudents(year, nen, kumi);
        List<StudentsCsv> csvs = students.stream()
                .map(e -> new StudentsCsv(e.getStudentId(), e.getLastName(), e.getFirstName(), e.getLastNameKana(),
                        e.getFirstNameKana(), e.getBirth(), e.getGender(), e.getFamily1(), e.getFamily2(), e.getTel1(),
                        e.getTel2(), e.getTel3(), e.getTel4(), e.getPostalCode(), e.getAdress(), e.getMemo()))
                .collect(Collectors.toList());
        // ファイルをダウンロード
        CsvMapper mapper = new CsvMapper();
        // 日付をフォーマット
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        CsvSchema schema = mapper.schemaFor(StudentsCsv.class).withHeader();
        return mapper.writer(schema).writeValueAsString(csvs);
    }

    // 生徒CSVアップロード ★一旦完成版
    public void upload(String userId, BufferedReader br) throws IOException {
        String line;
        // ヘッダーレコードをとばすためにあらかじめ１行だけ読み取っておく
        line = br.readLine();
        // 行がNULL（CSVの値がなくなる）になるまで処理を繰り返す
        while ((line = br.readLine()) != null) {
            // 負の数字を引数に指定し、中身が空でも、全ての要素を取得
            String[] split = line.split(",", -1);
            StudentForm studentForm = StudentForm.builder().studentId(split[0]).lastName(split[1]).firstName(split[2])
                    .lastNameKana(split[3]).firstNameKana(split[4]).birth(Date.valueOf(split[5]))
                    .gender((int) Integer.parseInt(split[6])).family1(split[7]).family2(split[8]).tel1(split[9])
                    .tel2(split[10]).tel3(split[11]).tel4(split[12]).postalCode(split[13]).adress(split[14])
                    .memo(split[15]).build();
            add(userId, studentForm);
//            Student student = new Student();
//            student.setStudentId(studentForm.getStudentId());
//            student.setLastName(studentForm.getLastName());
//            student.setFirstName(studentForm.getFirstName());
//            student.setLastNameKana(studentForm.getLastNameKana());
//            student.setFirstNameKana(studentForm.getFirstNameKana());
//            student.setBirth(studentForm.getBirth());
//            student.setGender(studentForm.getGender());
//            student.setFamily1(studentForm.getFamily1());
//            student.setFamily2(studentForm.getFamily2());
//            student.setTel1(studentForm.getTel1());
//            student.setTel2(studentForm.getTel2());
//            student.setTel3(studentForm.getTel3());
//            student.setTel4(studentForm.getTel4());
//            student.setPostalCode(studentForm.getPostalCode());
//            student.setAdress(studentForm.getAdress());
//            student.setMemo(studentForm.getMemo());
//            student.setCreatedBy(userId);
//            student.setUpdatedBy(userId);
//            studentRepository.save(student);
        }
    }

//    // 生徒CSVアップロード★★★お試し！
//    public void uploadCsv(String userId, BufferedReader br, StudentForm studentForm) throws IOException {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        String line;
//        // ヘッダーレコードをとばすためにあらかじめ１行だけ読み取っておく
//        line = br.readLine();
//        // 行がNULL（CSVの値がなくなる）になるまで処理を繰り返す
//        while ((line = br.readLine()) != null) {
//            // 負の数字を引数に指定し、中身が空でも、全ての要素を取得
//            
//            String[] split = line.split(",", -1);
//            Student student = Student.builder().studentId(split[0]).lastName(split[1]).firstName(split[2])
//                    .lastNameKana(split[3]).firstNameKana(split[4]).birth(format.parse(split[5]))
//                    .gender((int) Integer.parseInt(split[6])).family1(split[7]).family2(split[8]).tel1(split[9])
//                    .tel2(split[10]).tel3(split[11]).tel4(split[12]).postalCode(split[13]).adress(split[14])
//                    .memo(split[15]).build();
//        upload(userId, student);
//        studentForm.setStudentIds((List<String>) StudentForm.builder().studentId(split[0]).build());
//        studentForm.setLastNames((List<String>) StudentForm.builder().lastName(split[1]).build());
//        studentForm.setFirstNames((List<String>) StudentForm.builder().firstName(split[2]).build());
//        studentForm.setLastNameKanas((List<String>) StudentForm.builder().lastNameKana(split[3]).build());
//        studentForm.setFirstNameKanas((List<String>) StudentForm.builder().firstNameKana(split[4]).build());
//        studentForm.setBirths((List<Date>) StudentForm.builder().birth(format.parse(split[5])).build());
//        
//        }              
//    }

    // 生徒CSVアップロード
//    public void upload(String userId, Student student) {
////        // 入力値に対するバリデーション処理
////        List<String> errorMessage = validation(student);
////        if (errorMessage.size() != 0) {
////            return errorMessage;
////        }
//        student.setStudentId(student.getStudentId());
//        student.setLastName(student.getLastName());
//        student.setFirstName(student.getFirstName());
//        student.setLastNameKana(student.getLastNameKana());
//        student.setFirstNameKana(student.getFirstNameKana());
//        student.setBirth(student.getBirth());
//        student.setGender(student.getGender());
//        student.setFamily1(student.getFamily1());
//        student.setFamily2(student.getFamily2());
//        student.setTel1(student.getTel1());
//        student.setTel2(student.getTel2());
//        student.setTel3(student.getTel3());
//        student.setTel4(student.getTel4());
//        student.setPostalCode(student.getPostalCode());
//        student.setAdress(student.getAdress());
//        student.setMemo(student.getMemo());
//        student.setCreatedBy(userId);
//        student.setUpdatedBy(userId);
//        studentRepository.save(student);
//    }

//     private List<String> validation(Student student) {
//         List<String> errorMessage = new ArrayList<>();
//         // LastNameに対する必須項目チェック
//         if (StringUtils.isEmpty(student.getLastName())) {
//             errorMessage.add("LastNameが未入力です");
//         }
//
//         // tel1に対する必須項目チェック
//         if (Objects.isNull(student.getTel1())) {
//
//         }
//
//         return errorMessage;
//     } 

    // 生徒が削除可能か判定する
    // 生徒IDに紐づけられたクラス、出席簿、成績のデータがあるかどうか確認
    public boolean dataExists(String id) {
        boolean dataExists = false;
        List<StudentYear> studentYear = studentYearRepository.findAllByStudentIdOrderByYearAsc(id);
        List<Attendance> attendance = attendanceRepository.findAllByStudentId(id);
        List<Grade> grade = gradeRepository.findAllByStudentId(id);
        if (studentYear.size() > 0 || attendance.size() > 0 || grade.size() > 0) {
            dataExists = true;
        }
        return dataExists;
    }

    // 生徒削除
    public void delete(String id) {
        Student student = studentRepository.findById(id).orElseThrow();
        studentRepository.delete(student);
    }
}