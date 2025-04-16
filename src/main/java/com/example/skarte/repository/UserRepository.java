package com.example.skarte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.skarte.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUserId(String userId);
    
    List<User> findByOrderByUserIdAsc();
}