package com.example.skarte.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.Karte;

@Repository
public interface KarteRepository extends JpaRepository<Karte, Long> {

    // 更新日時の降順
    List<Karte> findByOrderByUpdatedAtDesc();

    // 生徒IDでリストを取得
    List<Karte> findAllByStudentIdOrderByDateDesc(String studentId);
    
    // 更新日時でリストを取得（指定日時以降）
    List<Karte> findByUpdatedAtAfterOrderByUpdatedAtDesc(LocalDateTime dateTime);
}