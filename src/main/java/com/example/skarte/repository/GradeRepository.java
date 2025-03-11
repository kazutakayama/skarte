package com.example.skarte.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    // 更新日時の降順で未削除の投稿（削除済みは表示しない）
//    List<Grade> findByDeletedFalseOrderByUpdatedAtDesc();
    List<Grade> findByOrderByUpdatedAtDesc();

    // 生徒IDでリストを取得
    List<Grade> findAllByStudentId(Long studentId);
}