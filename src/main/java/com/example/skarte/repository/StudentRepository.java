package com.example.skarte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    //更新日時の降順（削除済みは表示しない）　
//    List<Student> findByDeletedFalseOrderByUpdatedAtDesc();
    
    List<Student> findByOrderByUpdatedAtDesc();    
}