package com.example.skarte.service;

import java.util.List;

import com.example.skarte.entity.Grade;

//インターフェイス
public interface GradeService {

  /**
   * 成績全取得
   * 
   * @return
   */
  public List<Grade> findAll();

  /**
   * 成績1件取得
   * 
   * @param id
   * @return
   */
  public Grade findById(Long id);

  /**
   * 成績追加
   * 
   * @param grade
   * @return
   */
  public void addGrade(Grade grade);

  /**
   * 成績編集
   * 
   * @param grade
   * @return
   */
  public Grade updateGrade(Long gradeId, Grade grade);

  /**
   * 成績CSVダウンロード用
   * 
   * @param grade
   */
  public void insertGrade(Grade grade);

  /**
   * 成績削除（理論削除）
   * 
   * @param grade
   */
  public void deleteGrade(Long gradeId, Grade g);

}
