package com.example.skarte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;

@Repository
public interface StudentYearRepository extends JpaRepository<StudentYear, Long>, JpaSpecificationExecutor<StudentYear> {
    //更新日時の降順（削除済みは表示しない）　
//    List<StudentYear> findByDeletedFalseOrderByUpdatedAtDesc();
    
    List<StudentYear> findByOrderByUpdatedAtDesc();
    
    //生徒IDでリストを取得
    List<StudentYear> findAllByStudentIdOrderByYearAsc(String studentId);
//    List<StudentYear> findAllByStudentId(String studentId);
    
 // 年度でリストを取得
    List<StudentYear> findAllByYear(Long year);
    

//    
}