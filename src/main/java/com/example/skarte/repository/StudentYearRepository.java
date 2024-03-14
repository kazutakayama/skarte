package com.example.skarte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.StudentYear;


@Repository
public interface StudentYearRepository extends JpaRepository<StudentYear, Long> {
    //更新日時の降順（削除済みは表示しない）　
    List<StudentYear> findByDeletedFalseOrderByUpdatedAtDesc();
}