package com.example.skarte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Notice;
import com.example.skarte.repository.NoticeRepository;

// 【2/20 関解説予定】インターフェースを作りましょう
@Service
public class NoticesService {

    // 【余裕があったら】コンストラクタインジェクションに修正しましょう
    @Autowired
    NoticeRepository noticeRepository;

    public List<Notice> findAll() {        
        return noticeRepository.findByDeletedFalseOrderByUpdatedAtDesc();
    }

    public Notice save(Notice notice) {
        return noticeRepository.save(notice);
    }

    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElseThrow();
    }
    
//　　　　物理削除用
//    public void delete(Long id) {
//        Notice notice = noticeRepository.findById(id).orElseThrow();
//        noticeRepository.delete(notice);
//    }
 
    
}

