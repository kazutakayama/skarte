package com.example.skarte.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.skarte.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(Long userId);
}