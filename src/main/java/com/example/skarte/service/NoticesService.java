package com.example.skarte.service;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;

import com.example.skarte.entity.Notice;
//import com.example.skarte.repository.NoticeRepository;

//@Service
//public class NoticesService {

//インターフェイス
public interface NoticesService {

    /**
     * お知らせ全取得
     * 
     * @return
     */
    public List<Notice> findAll();

    /**
     * お知らせ1件取得
     * 
     * @param id
     * @return
     */
    public Notice findById(Long id);

    /**
     * お知らせ追加
     * 
     * @param notice
     * @return
     */
    public void add(Notice notice);

    /**
     * お知らせ編集
     * 
     * @param notice
     * @return
     */
    public void update(Long noticeId, Notice notice);

    /**
     * お知らせ削除
     * 
     * @param notice
     */
    public void delete(Long noticeId, Notice notice);
}

// 【余裕があったら】コンストラクタインジェクションに修正しましょう
//    @Autowired
//    NoticeRepository noticeRepository;
//
//    public List<Notice> findAll() {        
//        return noticeRepository.findByDeletedFalseOrderByUpdatedAtDesc();
//    }
//
//    public Notice save(Notice notice) {
//        return noticeRepository.save(notice);
//    }
//
//    public Notice findById(Long id) {
//        return noticeRepository.findById(id).orElseThrow();
//    }

//　　　　物理削除用
//    public void delete(Long id) {
//        Notice notice = noticeRepository.findById(id).orElseThrow();
//        noticeRepository.delete(notice);
//    }

//}
