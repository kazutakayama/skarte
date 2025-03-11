package com.example.skarte.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.skarte.entity.StudentYear;
import com.example.skarte.repository.StudentYearRepository;
import com.example.skarte.service.StudentsYearService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentsYearServiceImpl implements StudentsYearService {

    private final StudentYearRepository studentYearRepository;

    /**
     * クラス全取得
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentYear> findAll() {
//        return studentYearRepository.findByDeletedFalseOrderByUpdatedAtDesc();
        return studentYearRepository.findByOrderByUpdatedAtDesc();
    }
    
    /**
     * 生徒IDでリストを取得
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentYear> findAllByStudentId(Long studentId) {
        return studentYearRepository.findAllByStudentId(studentId);
    }

    /**
     * クラス1件取得
     * 
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public StudentYear findById(Long id) {
        return studentYearRepository.findById(id).orElseThrow();
    }

    /**
     * クラス追加
     * 
     * @param studentYear
     * @return
     */
    @Override
    @Transactional
    public void addClass(Long userId, StudentYear studentYear) {
        studentYear.setCreatedBy(userId);
        studentYear.setUpdatedBy(userId);
        studentYearRepository.save(studentYear);
    }

    /**
     * クラス編集
     * 
     * @param studentYear
     * @return
     */
    @Override
    @Transactional
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
    @Override
    @Transactional
    public void insert(StudentYear studentYear) {
        studentYearRepository.save(studentYear);
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