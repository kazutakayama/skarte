package com.example.skarte.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Notice;
import com.example.skarte.form.NoticeForm;
import com.example.skarte.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticesService {

    private final NoticeRepository noticeRepository;

    /** お知らせ全取得 */
    public List<Notice> findAll() {
        return noticeRepository.findByOrderByUpdatedAtDesc();
    }

    /** お知らせ全取得（内容をリンクにしたもの）→th:utextで画面表示 */
    public List<Notice> convertUrlsToLinks() {
        List<Notice> notices = findAll();
        for (int i = 0; i < notices.size(); i++) {
            notices.get(i).setContents(convertUrlsToLinks(notices.get(i).getContents()));
            notices.get(i).setContents(sanitizeHtml(notices.get(i).getContents()));
        }
        return notices;
    }

    /** 最近１週間に更新されたカルテを取得 */
    public List<Notice> recentNotice() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<Notice> recentNotice = noticeRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(oneWeekAgo);
        return recentNotice;
    }

    /** お知らせ１件取得 */
    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElseThrow();
    }

    /** お知らせ追加 */
    public void add(String userId, NoticeForm noticeForm) {
        Notice notice = new Notice();
        notice.setContents(noticeForm.getContents());
        notice.setCreatedBy(userId);
        notice.setUpdatedBy(userId);
        noticeRepository.save(notice);
    }

    /** お知らせ更新 */
    public void update(Long id, NoticeForm noticeForm, String userId) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        notice.setContents(noticeForm.getContents());
        notice.setUpdatedBy(userId);
        noticeRepository.save(notice);
    }

    /** お知らせ削除 */
    public void delete(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        noticeRepository.delete(notice);
    }

    /** お知らせの内容をリンクに変換 */
    public String convertUrlsToLinks(String text) {
        String urlRegex = "(https?://[^\\s\"<>]+)";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String url = matcher.group();
            String anchor = "<a href=\"" + url + "\" target=\"_blank\" rel=\"noopener noreferrer\">" + url + "</a>";
            matcher.appendReplacement(sb, Matcher.quoteReplacement(anchor));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /** お知らせの内容をサニタイズ（XSSの防止）※aタグは有効 */
    public String sanitizeHtml(String input) {
        return Jsoup.clean(input, Safelist.basicWithImages().addTags("a"));
    }
}
