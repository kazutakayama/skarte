package com.example.skarte.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.User;
import com.example.skarte.form.PasswordForm;
import com.example.skarte.form.UserEditForm;
import com.example.skarte.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /** 教師全取得 */
    public List<User> findAll() {
        return userRepository.findByOrderByUserIdAsc();
    }

    /** 教師１件取得 */
    public User findById(String userId) {
        return userRepository.findByUserId(userId);
    }

    /** 教師更新（管理者から） */
    public void updateByAdmin(String id, UserEditForm form, String userId) {
        if (id.equals(userId)) {
            throw new IllegalArgumentException("本人の情報は変更できません");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        user.setLastName(form.getLastName());
        user.setFirstName(form.getFirstName());
        user.setAuthority(form.getAuthority());
        user.setUpdatedBy(userId);
        userRepository.save(user);
    }

    /** 教師削除（管理者から） */
    public void delete(String id) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
    }

    /** ユーザー情報更新（ユーザー本人から） */
    public void update(String id, UserEditForm form, String userId) {
        if (id.equals(userId)) {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
            user.setLastName(form.getLastName());
            user.setFirstName(form.getFirstName());
            user.setUpdatedBy(userId);
            userRepository.save(user);
            // 認証情報を更新する
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName().equals(user.getUserId())) {
                UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(user,
                        user.getPassword(), user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
        }
    }

    /** パスワード更新（ユーザー本人から） */
    public boolean updatePassword(User user, PasswordForm form) {
        // 現在のパスワードが一致しているか確認
        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setUpdatedBy(user.getUserId());
        userRepository.save(user);
        // 認証情報を更新する
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName().equals(user.getUserId())) {
            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(user,
                    user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
        return true;
    }

    /** userIdと名前の紐づけ */
    public Map<String, String> updateUser() {
        Map<String, String> updateUser = new HashMap<>();
        List<User> users = userRepository.findByOrderByUserIdAsc();
        for (int i = 0; i < users.size(); i++) {
            updateUser.put(users.get(i).getUserId(), users.get(i).getLastName() + " " + users.get(i).getFirstName());
        }
        return updateUser;
    }

}
