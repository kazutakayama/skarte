package com.example.skarte.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {

    // 更新日時の降順
    List<Attendance> findByOrderByUpdatedAtDesc();

    // 生徒IDでリストを取得
    List<Attendance> findAllByStudentId(String studentId);
}