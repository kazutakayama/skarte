package com.example.skarte.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.Grade;
import com.example.skarte.entity.StudentYear;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long>, JpaSpecificationExecutor<Grade> {

    // 更新日時の降順で未削除の投稿（削除済みは表示しない）
//    List<Grade> findByDeletedFalseOrderByUpdatedAtDesc();
    List<Grade> findByOrderByUpdatedAtDesc();

    // 生徒IDでリストを取得
    List<Grade> findAllByStudentId(String studentId);
    
    List<Grade> findAllByYear(Long year);
    
//    List<Grade> findAllByStudentIdAndYear(Long year);
    
}