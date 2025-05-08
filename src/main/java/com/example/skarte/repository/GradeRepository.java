package com.example.skarte.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long>, JpaSpecificationExecutor<Grade> {

    // 更新日時の降順
    List<Grade> findByOrderByUpdatedAtDesc();

    // 生徒IDでリストを取得
    List<Grade> findAllByStudentId(String studentId);
    
    // 年度でリストを取得
    List<Grade> findAllByYear(Long year);
    
}