package com.example.skarte.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Karte;
import com.example.skarte.form.KarteForm;
import com.example.skarte.repository.KarteRepository;

@Service
public class KarteService {

    @Autowired
    KarteRepository karteRepository;

    /**カルテ全取得*/
    public List<Karte> findAll() {
        return karteRepository.findByOrderByUpdatedAtDesc();
    }

    /**生徒IDでリストを取得*/
    public List<Karte> findAllByStudentId(String studentId) {
        return karteRepository.findAllByStudentIdOrderByDateDesc(studentId);
    }

    /**カルテ1件取得*/
    public Karte findById(Long id) {
        return karteRepository.findById(id).orElseThrow();
    }

    /**最近１週間に更新されたカルテを取得*/
    public List<Karte> recentKarte() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<Karte> recentKarte = karteRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(oneWeekAgo);
        return recentKarte;
    }

    /**カルテ追加*/
    public void add(String studentId, String userId, KarteForm karteForm) {
        Karte karte = new Karte();
        karte.setStudentId(studentId);
        karte.setDate(karteForm.getDate());
        karte.setContents(karteForm.getContents());
        karte.setCreatedBy(userId);
        karte.setUpdatedBy(userId);
        karteRepository.save(karte);
    }

    /**カルテ更新*/
    public void update(Long id, String userId, KarteForm karteForm) {
        Karte karte = karteRepository.findById(id).orElseThrow();
        karte.setDate(karteForm.getDate());
        karte.setContents(karteForm.getContents());
        karte.setUpdatedBy(userId);
        karteRepository.save(karte);
    }

    /**カルテ削除*/
    public void delete(Long id) {
        Karte karte = karteRepository.findById(id).orElseThrow();
        karteRepository.delete(karte);
    }
}