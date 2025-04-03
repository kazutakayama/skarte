package com.example.skarte.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.StudentYear;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // 更新日時の降順で未削除の投稿（削除済みは表示しない）
//    List<Attendance> findByDeletedFalseOrderByUpdatedAtDesc();
    List<Attendance> findByOrderByUpdatedAtDesc();

    // 生徒IDでリストを取得
    List<Attendance> findAllByStudentId(String studentId);
}