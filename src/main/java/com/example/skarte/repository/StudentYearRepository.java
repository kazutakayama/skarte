package com.example.skarte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.StudentYear;

@Repository
public interface StudentYearRepository extends JpaRepository<StudentYear, Long>, JpaSpecificationExecutor<StudentYear> {
    
    //更新日時の降順    
    List<StudentYear> findByOrderByUpdatedAtDesc();
    
    //生徒IDでリストを取得
    List<StudentYear> findAllByStudentIdOrderByYearAsc(String studentId);
    
    // 年度でリストを取得
    List<StudentYear> findAllByYear(Long year);
  
}