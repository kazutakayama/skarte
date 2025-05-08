package com.example.skarte.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 更新日時の降順
    List<Notice> findByOrderByUpdatedAtDesc();

    // 更新日時でリストを取得（指定日時以降）
    List<Notice> findByUpdatedAtAfterOrderByUpdatedAtDesc(LocalDateTime dateTime);
}
