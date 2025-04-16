package com.example.skarte.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

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

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
//import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentsService {

    private final StudentRepository studentRepository;
    private final StudentYearRepository studentYearRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;
    private final StudentSpecification studentSpecification;

//    @Autowired
//    StudentRepository studentRepository;
//
//    StudentYearRepository studentYearRepository;
//
//    StudentSpecification studentSpecification;

    // 生徒全取得
    public List<Student> findAll() {
        return studentRepository.findByOrderByUpdatedAtDesc();
    }

    // 生徒１件取得
    public Student findById(String id) {
        return studentRepository.findById(id).orElseThrow();
    }

//    // 性別のMap
//    public Map<Integer, String> gender(){
//        Map<Integer, String> gender = new HashMap<>();
//        gender.put(1, "男");
//        gender.put(2, "女");
//        gender.put(3, "他");
//        return gender;
//    }

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

    // 生徒CSVアップロード
    public void upload(String userId, Student student) {
//        // 入力値に対するバリデーション処理
//        List<String> errorMessage = validation(student);
//        if (errorMessage.size() != 0) {
//            return errorMessage;
//        }
        student.setStudentId(student.getStudentId());
        student.setLastName(student.getLastName());
        student.setFirstName(student.getFirstName());
        student.setLastNameKana(student.getLastNameKana());
        student.setFirstNameKana(student.getFirstNameKana());
        student.setBirth(student.getBirth());
        student.setGender(student.getGender());
        student.setFamily1(student.getFamily1());
        student.setFamily2(student.getFamily2());
        student.setTel1(student.getTel1());
        student.setTel2(student.getTel2());
        student.setTel3(student.getTel3());
        student.setTel4(student.getTel4());
        student.setPostalCode(student.getPostalCode());
        student.setAdress(student.getAdress());
        student.setMemo(student.getMemo());
//
        student.setCreatedBy(userId);
        student.setUpdatedBy(userId);
        studentRepository.save(student);
    }

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