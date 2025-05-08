package com.example.skarte.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.skarte.entity.User;
import com.example.skarte.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UserRepository userRepository;

    // 教師全取得
    public List<User> findAll() {
        return userRepository.findByOrderByUserIdAsc();
    }
    
    // userIdと名前の紐づけ
    public Map<String, String> updateUser() {
        Map<String, String> updateUser = new HashMap<>();
        List<User> users = userRepository.findByOrderByUserIdAsc();
        for (int i = 0; i < users.size(); i++) {
            updateUser.put(users.get(i).getUserId(), users.get(i).getLastName() + " " + users.get(i).getFirstName());
        }
        return updateUser;
    }

}
