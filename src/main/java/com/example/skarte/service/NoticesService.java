package com.example.skarte.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Karte;
import com.example.skarte.entity.Notice;
import com.example.skarte.entity.User;
import com.example.skarte.form.NoticeForm;
import com.example.skarte.repository.AttendanceRepository;
import com.example.skarte.repository.NoticeRepository;
import com.example.skarte.repository.StudentRepository;
import com.example.skarte.repository.StudentYearRepository;
import com.example.skarte.repository.UserRepository;
import com.example.skarte.specification.StudentSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticesService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

//    @Autowired
//    NoticeRepository noticeRepository;

    // お知らせ全取得
    public List<Notice> findAll() {
        return noticeRepository.findByOrderByUpdatedAtDesc();
    }

//    // お知らせ全件にユーザー名をつけて取得
//    public Map<Notice, String> map() {
//        Map<Notice, String> map = new HashMap<>();
//        List<Notice> notice = noticeRepository.findByOrderByUpdatedAtDesc();
//        for (int i = 0; i < notice.size(); i++) {
//            User user = userRepository.findById(notice.get(i).getUpdatedBy()).orElseThrow();
//            map.put(notice.get(i), user.getLastName() + " " + user.getFirstName());
//        }       
//        return map;
//    }

    // お知らせ１件取得
    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElseThrow();
    }

    /**
     * 最近１週間に更新されたカルテを取得
     */
    public List<Notice> recentNotice() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<Notice> recentNotice = noticeRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(oneWeekAgo);
        return recentNotice;
    }

//    // お知らせ1件にユーザー名をつけて取得
//    public Map<Notice, String> map(Long id) {
//        Map<Notice, String> map = new HashMap<>();
//        Notice notice = noticeRepository.findById(id).orElseThrow();
//        User user = userRepository.findById(notice.getUpdatedBy()).orElseThrow();
//        map.put(notice, user.getLastName() + " " + user.getFirstName());
//        return map;
//    }

    // お知らせ追加
    public void add(String userId, NoticeForm noticeForm) {
        Notice notice = new Notice();
//            notice.setTitle(noticeForm.getTitle());
        notice.setContents(noticeForm.getContents());
        notice.setCreatedBy(userId);
        notice.setUpdatedBy(userId);
        noticeRepository.save(notice);
    }

    // お知らせ更新
    public void update(Long id, NoticeForm noticeForm, String userId) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
//        notice.setTitle(noticeForm.getTitle());
        notice.setContents(noticeForm.getContents());
        notice.setUpdatedBy(userId);
        noticeRepository.save(notice);
    }

    // お知らせ削除
    public void delete(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        noticeRepository.delete(notice);
    }
}
