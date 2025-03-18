//package com.example.skarte.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.skarte.entity.Student;
//import com.example.skarte.entity.StudentYear;
//import com.example.skarte.form.StudentForm;
//import com.example.skarte.repository.StudentRepository;
//import com.example.skarte.repository.StudentYearRepository;
//
//@Service
//public class StudentsYearService {
//
//    @Autowired
//    StudentRepository studentRepository;
//    StudentYearRepository studentYearRepository;
//
//    // クラス全取得
//    public List<StudentYear> findAll() {
//        return studentYearRepository.findByOrderByUpdatedAtDesc();
//    }
//
////    /**
////     * 生徒IDでリストを取得
////     * 
////     * @return
////     */
//    public List<StudentYear> findAllByStudentId(Long studentId) {
//        return studentYearRepository.findAllByStudentId(studentId);
//    }
//
//    // クラス１件取得
//    public StudentYear findById(Long id) {
//        return studentYearRepository.findById(id).orElseThrow();
//    }
//
//    /**
//     * クラス追加
//     * 
//     * @param userId
//     * @param student
//     * @return
//     */
//    public void addClass(Long userId, StudentYear studentYear) {
//        studentYear.setCreatedBy(userId);
//        studentYear.setUpdatedBy(userId);
//        studentYearRepository.save(studentYear);
//    }
//
//    /**
//     * クラス編集
//     * 
//     * @param student
//     * @return
//     */
//    public StudentYear updateClass(Long studentYearId, StudentYear studentYear) {
//        StudentYear targetStudentYear = studentYearRepository.findById(studentYearId).orElseThrow();
//        targetStudentYear.setYear(studentYear.getYear());
//        targetStudentYear.setNen(studentYear.getNen());
//        targetStudentYear.setKumi(studentYear.getKumi());
//        targetStudentYear.setBan(studentYear.getBan());
//        targetStudentYear.setUpdatedBy(studentYear.getUpdatedBy());
//        studentYearRepository.save(targetStudentYear);
//        return targetStudentYear;
//    }
//
//    /**
//     * クラスCSVダウンロード用
//     * 
//     * @param student
//     */
//    public void insert(StudentYear studentYear) {
//        studentYearRepository.save(studentYear);
//    }
//
////    /**
////     * クラス削除（理論削除）
////     * 
////     * @param student
////     */
////    public void deleteClass(Long studentYearId, StudentYear studentYear);
//
//}

//package com.example.skarte.service;
//
//import java.util.List;
//
//import com.example.skarte.entity.StudentYear;
//
////インターフェイス
//public interface StudentsYearService {
//
//    /**
//     * クラス全取得
//     * 
//     * @return
//     */
//    public List<StudentYear> findAll();
//    
//    /**
//     * 生徒IDでリストを取得
//     * 
//     * @return
//     */
//    public List<StudentYear> findAllByStudentId(Long studentId);
//
//
//    /**
//     * クラス1件取得
//     * 
//     * @param id
//     * @return
//     */
//    public StudentYear findById(Long id);
//
//    
//    /**
//     * クラス追加
//     * 
//     * @param userId
//     * @param student
//     * @return
//     */
//    public void addClass(Long userId, StudentYear studentYear);
//
//    /**
//     * クラス編集
//     * 
//     * @param student
//     * @return
//     */
//    public StudentYear updateClass(Long studentYearId, StudentYear studentYear);
//
//    /**
//     * クラスCSVダウンロード用
//     * 
//     * @param student
//     */
//    public void insert(StudentYear studentYear);
//
////    /**
////     * クラス削除（理論削除）
////     * 
////     * @param student
////     */
////    public void deleteClass(Long studentYearId, StudentYear studentYear);
//
//}

package com.example.skarte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.repository.StudentYearRepository;

@Service
public class StudentsYearService {

    @Autowired

    StudentYearRepository studentYearRepository;

    /**
     * クラス全取得
     * 
     * @return
     */
    public List<StudentYear> findAll() {
//        return studentYearRepository.findByDeletedFalseOrderByUpdatedAtDesc();
        return studentYearRepository.findByOrderByUpdatedAtDesc();
    }

    /**
     * 生徒IDでリストを取得
     * 
     * @return
     */
    public List<StudentYear> findAllByStudentId(Long studentId) {
        return studentYearRepository.findAllByStudentId(studentId);
    }

    /**
     * クラス1件取得
     * 
     * @param id
     * @return
     */
    public StudentYear findById(Long id) {
        return studentYearRepository.findById(id).orElseThrow();
    }

    /**
     * クラス追加
     * 
     * @param studentYear
     * @return
     */
    public void addClass(Long userId, StudentYear studentYear) {
        studentYear.setCreatedBy(userId);
        studentYear.setUpdatedBy(userId);
        studentYearRepository.save(studentYear);
    }

    /**
     * クラス更新
     * 
     * @param studentYear
     * @return
     */
    public StudentYear updateClass(Long studentYearId, StudentYear studentYear) {
        StudentYear targetStudentYear = studentYearRepository.findById(studentYearId).orElseThrow();
        targetStudentYear.setYear(studentYear.getYear());
        targetStudentYear.setNen(studentYear.getNen());
        targetStudentYear.setKumi(studentYear.getKumi());
        targetStudentYear.setBan(studentYear.getBan());
        targetStudentYear.setUpdatedBy(studentYear.getUpdatedBy());
        studentYearRepository.save(targetStudentYear);
        return targetStudentYear;
//        studentRepository.save(student);
    }

    /**
     * クラスCSVダウンロード用
     * 
     * @param studentYear
     */
    public void insert(StudentYear studentYear) {
        studentYearRepository.save(studentYear);
    }

    // クラス削除
    public void deleteClass(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow();
        studentYearRepository.delete(studentYear);
    }
//    /**
//     * 生徒削除（理論削除）　→　転出に変更
//     * 
//     * @param studentYear
//     */
//    @Override
//    @Transactional
//    public void deleteClass(Long studentYearId, StudentYear studentYear) {
//        StudentYear targetStudentYear = studentYearRepository.findById(studentYearId).orElseThrow();
//        targetStudentYear.setUpdatedBy(studentYear.getUpdatedBy());
//        targetStudentYear.setDeleted(Boolean.TRUE);
//        studentYearRepository.save(targetStudentYear);
//    }
}