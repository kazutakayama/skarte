package com.example.skarte.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.skarte.entity.Karte;
import com.example.skarte.entity.StudentYear;
import com.example.skarte.repository.KarteRepository;
import com.example.skarte.service.KarteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KarteServiceImpl implements KarteService {

    private final KarteRepository karteRepository;

    /**
     * カルテ全取得
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Karte> findAll() {
        return karteRepository.findByDeletedFalseOrderByUpdatedAtDesc();
    }
    
    /**
     * 生徒IDでリストを取得
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Karte> findAllByStudentId(Long studentId) {
        return karteRepository.findAllByStudentId(studentId);
    }


    /**
     * カルテ1件取得
     * 
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Karte findById(Long id) {
        return karteRepository.findById(id).orElseThrow();
    }

    /**
     * カルテ追加
     * 
     * @param karte
     * @return
     */
    @Override
    @Transactional
    public void addKarte(Karte karte) {
        karteRepository.save(karte);
    }

    /**
     * カルテ編集
     * 
     * @param karte
     * @return
     */
    @Override
    @Transactional
    public Karte updateKarte(Long karteId, Karte karte) {
        Karte targetKarte = karteRepository.findById(karteId).orElseThrow();
        targetKarte.setDate(karte.getDate());
        targetKarte.setContents(karte.getContents());
        return targetKarte;
//        studentRepository.save(student);
    }

    /**
     * カルテCSVダウンロード用
     * 
     * @param karte
     */
    @Override
    @Transactional
    public void insertKarte(Karte karte) {
        karteRepository.save(karte);
    }

    /**
     * カルテ削除（理論削除）
     * 
     * @param karte
     */
    @Override
    @Transactional
    public void deleteKarte(Long karteId, Karte karte) {
        Karte targetKarte = karteRepository.findById(karteId).orElseThrow();
        targetKarte.setDeleted(Boolean.TRUE);
        karteRepository.save(targetKarte);
    }
}