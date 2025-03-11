package com.example.skarte.service;

import java.util.List;

import com.example.skarte.entity.StudentYear;

//インターフェイス
public interface StudentsYearService {

    /**
     * クラス全取得
     * 
     * @return
     */
    public List<StudentYear> findAll();
    
    /**
     * 生徒IDでリストを取得
     * 
     * @return
     */
    public List<StudentYear> findAllByStudentId(Long studentId);


    /**
     * クラス1件取得
     * 
     * @param id
     * @return
     */
    public StudentYear findById(Long id);

    
    /**
     * クラス追加
     * 
     * @param userId
     * @param student
     * @return
     */
    public void addClass(Long userId, StudentYear studentYear);

    /**
     * クラス編集
     * 
     * @param student
     * @return
     */
    public StudentYear updateClass(Long studentYearId, StudentYear studentYear);

    /**
     * クラスCSVダウンロード用
     * 
     * @param student
     */
    public void insert(StudentYear studentYear);

//    /**
//     * クラス削除（理論削除）
//     * 
//     * @param student
//     */
//    public void deleteClass(Long studentYearId, StudentYear studentYear);

}
