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
        return studentYearRepository.findByDeletedFalseOrderByUpdatedAtDesc();
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
     * @param student
     * @return
     */
    @Override
    @Transactional
    public void addClass(StudentYear studentYear) {
        studentYearRepository.save(studentYear);
    }

    /**
     * クラス編集
     * 
     * @param student
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
        studentYearRepository.save(targetStudentYear);
        return targetStudentYear;
//        studentRepository.save(student);
    }

    /**
     * 生徒CSVダウンロード用
     * 
     * @param student
     */
    @Override
    @Transactional
    public void insert(StudentYear studentYear) {
        studentYearRepository.save(studentYear);
    }

    /**
     * 生徒削除（理論削除）
     * 
     * @param student
     */
    @Override
    @Transactional
    public void deleteClass(Long studentYearId, StudentYear studentYear) {
        StudentYear targetStudentYear = studentYearRepository.findById(studentYearId).orElseThrow();
        targetStudentYear.setDeleted(Boolean.TRUE);
        studentYearRepository.save(targetStudentYear);
    }
}