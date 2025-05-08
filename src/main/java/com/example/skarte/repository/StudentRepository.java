package com.example.skarte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> , JpaSpecificationExecutor<Student>{

    // 更新日時の降順
    List<Student> findByOrderByUpdatedAtDesc();
    
    // 生徒ID
    Student findByStudentId(String studentId);

}