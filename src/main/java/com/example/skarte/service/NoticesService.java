package com.example.skarte.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Notice;
import com.example.skarte.form.NoticeForm;
import com.example.skarte.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticesService {

    private final NoticeRepository noticeRepository;

    /**お知らせ全取得 */
    public List<Notice> findAll() {
        return noticeRepository.findByOrderByUpdatedAtDesc();
    }

    /**お知らせ１件取得 */
    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElseThrow();
    }

    /**最近１週間に更新されたカルテを取得*/
    public List<Notice> recentNotice() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<Notice> recentNotice = noticeRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(oneWeekAgo);
        return recentNotice;
    }

    /**お知らせ追加 */
    public void add(String userId, NoticeForm noticeForm) {
        Notice notice = new Notice();
        notice.setContents(noticeForm.getContents());
        notice.setCreatedBy(userId);
        notice.setUpdatedBy(userId);
        noticeRepository.save(notice);
    }

    /**お知らせ更新 */
    public void update(Long id, NoticeForm noticeForm, String userId) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        notice.setContents(noticeForm.getContents());
        notice.setUpdatedBy(userId);
        noticeRepository.save(notice);
    }

    /**お知らせ削除 */
    public void delete(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        noticeRepository.delete(notice);
    }
}
