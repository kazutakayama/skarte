package com.example.skarte.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.skarte.entity.Notice;
import com.example.skarte.repository.NoticeRepository;
import com.example.skarte.service.NoticesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticesServiceImpl implements NoticesService {

    private final NoticeRepository noticeRepository;

    /**
     * お知らせ全取得
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Notice> findAll() {
        return noticeRepository.findByDeletedFalseOrderByUpdatedAtDesc();
    }

    /**
     * お知らせ1件取得
     * 
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElseThrow();
    }

    /**
     * お知らせ追加
     * 
     * @param notice
     * @return
     */
    @Override
    @Transactional
    public void add(Notice notice) {
        noticeRepository.save(notice);
    }

    /**
     * お知らせ編集
     * 
     * @param notice
     * @return
     */
    @Override
    @Transactional
    public void update(Long noticeId, Notice notice) {
        Notice targetNotice = noticeRepository.findById(noticeId).orElseThrow();
        targetNotice.setTitle(notice.getTitle());
        targetNotice.setContents(notice.getContents());
        noticeRepository.save(targetNotice);
    }

    /**
     * お知らせ削除（理論削除）
     * 
     * @param notice
     */
    @Override
    @Transactional
    public void delete(Long noticeId, Notice notice) {
        Notice targetNotice = noticeRepository.findById(noticeId).orElseThrow();
        targetNotice.setDeleted(Boolean.TRUE);
        noticeRepository.save(targetNotice);
    }
}