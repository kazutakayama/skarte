package com.example.skarte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Karte;
import com.example.skarte.repository.AttendanceRepository;
import com.example.skarte.repository.GradeRepository;

@Service
public class GradeService {

    @Autowired
    GradeRepository gradeRepository;

    /**
     * 成績全取得
     * 
     * @return
     */
    public List<Grade> findAll() {
        return gradeRepository.findByOrderByUpdatedAtDesc();
    }

    /**
     * 生徒IDでリストを取得
     * 
     * @return
     */
    public List<Grade> findAllByStudentId(Long studentId) {
        return gradeRepository.findAllByStudentId(studentId);
    }

    /**
     * 成績1件取得
     * 
     * @param id
     * @return
     */
    public Grade findById(Long id) {
        return gradeRepository.findById(id).orElseThrow();
    }

    /**
     * 成績追加
     * 
     * @param grade
     * @return
     */
    public void addGrade(Long userId, Long studentId, Grade grade) {
        grade.setStudentId(grade.getStudentId());
        grade.setCreatedBy(userId);
        grade.setUpdatedBy(userId);
        gradeRepository.save(grade);
    }

    /**
     * 成績更新
     * 
     * @param grade
     * @return
     */
    public Grade updateGrade(Long gradeId, Long studentId, Grade grade) {
        grade.setStudentId(grade.getStudentId());
        Grade targetGrade = gradeRepository.findById(gradeId).orElseThrow();
        targetGrade.setYear(grade.getYear());
        targetGrade.setTerm(grade.getTerm());
        targetGrade.setSubject(grade.getSubject());
        targetGrade.setRating(grade.getRating());
        targetGrade.setUpdatedBy(grade.getUpdatedBy());
        gradeRepository.save(targetGrade);
        return targetGrade;
    }

//    /**
//     * 成績更新
//     * 
//     * @param grade
//     * @return
//     */
//    public Grade updateGrade(Long gradeId, Grade grade) {
//        Grade targetGrade = gradeRepository.findById(gradeId).orElseThrow();
//        targetGrade.setYear(grade.getYear());
//        targetGrade.setTerm(grade.getTerm());
//        targetGrade.setSubject(grade.getSubject());
//        targetGrade.setGrade(grade.getGrade());
//        targetGrade.setUpdatedBy(grade.getUpdatedBy());
//        gradeRepository.save(targetGrade);
//        return gradeRepository.save(targetGrade);
//    }

    /**
     * 成績削除
     * 
     * @param grade
     */
    public Grade deleteGrade(Long gradeId, Long studentId, Grade grade) {
        grade.setStudentId(grade.getStudentId());
        Grade deleteGrade = gradeRepository.findById(gradeId).orElseThrow();
        gradeRepository.delete(deleteGrade);
        return deleteGrade;
    }

//    /**
//     * 成績CSVダウンロード用
//     * 
//     * @param grade
//     */
//    public void insertGrade(Grade grade) {
//        gradeRepository.save(grade);
//    }

//  /**
//   * 成績削除（理論削除）
//   * 
//   * @param grade
//   */
//  public void deleteGrade(Long gradeId, Grade g);

}

//package com.example.skarte.service;
//
//import java.util.List;
//
//import com.example.skarte.entity.Grade;
//
////インターフェイス
//public interface GradeService {
//
//  /**
//   * 成績全取得
//   * 
//   * @return
//   */
//  public List<Grade> findAll();
//  
//  /**
//   * 生徒IDでリストを取得
//   * 
//   * @return
//   */
//  public List<Grade> findAllByStudentId(Long studentId);
//
//  /**
//   * 成績1件取得
//   * 
//   * @param id
//   * @return
//   */
//  public Grade findById(Long id);
//
//  /**
//   * 成績追加
//   * 
//   * @param grade
//   * @return
//   */
//  public void addGrade(Grade grade);
//
//  /**
//   * 成績編集
//   * 
//   * @param grade
//   * @return
//   */
//  public Grade updateGrade(Long gradeId, Grade grade);
//
//  /**
//   * 成績CSVダウンロード用
//   * 
//   * @param grade
//   */
//  public void insertGrade(Grade grade);
//
////  /**
////   * 成績削除（理論削除）
////   * 
////   * @param grade
////   */
////  public void deleteGrade(Long gradeId, Grade g);
//
//}
