package com.example.skarte.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.skarte.entity.Grade;
import com.example.skarte.repository.GradeRepository;
import com.example.skarte.service.GradeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;

    /**
     * 成績全取得
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Grade> findAll() {
        return gradeRepository.findByDeletedFalseOrderByUpdatedAtDesc();
    }
    
    /**
     * 生徒IDでリストを取得
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Grade> findAllByStudentId(Long studentId) {
        return gradeRepository.findAllByStudentId(studentId);
    }


    /**
     * 成績1件取得
     * 
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Grade findById(Long id) {
        return gradeRepository.findById(id).orElseThrow();
    }

    /**
     * 成績追加
     * 
     * @param grade
     * @return
     */
    @Override
    @Transactional
    public void addGrade(Grade grade) {
        gradeRepository.save(grade);
    }

    /**
     * 成績編集
     * 
     * @param grade
     * @return
     */
    @Override
    @Transactional
    public Grade updateGrade(Long gradeId, Grade grade) {
        Grade targetGrade = gradeRepository.findById(gradeId).orElseThrow();
        targetGrade.setYear(grade.getYear());
        return targetGrade;
    }

    /**
     * 成績CSVダウンロード用
     * 
     * @param grade
     */
    @Override
    @Transactional
    public void insertGrade(Grade grade) {
        gradeRepository.save(grade);
    }

    /**
     * 成績削除（理論削除）
     * 
     * @param grade
     */
    @Override
    @Transactional
    public void deleteGrade(Long gradeId, Grade grade) {
        Grade targetGrade = gradeRepository.findById(gradeId).orElseThrow();
        targetGrade.setDeleted(Boolean.TRUE);
        gradeRepository.save(targetGrade);
    }
}