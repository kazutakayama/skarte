package com.example.skarte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skarte.entity.Karte;
import com.example.skarte.entity.Student;
import com.example.skarte.form.KarteForm;
import com.example.skarte.repository.KarteRepository;
import com.example.skarte.repository.StudentRepository;
import com.example.skarte.repository.StudentYearRepository;

@Service
public class KarteService {

    @Autowired
    KarteRepository karteRepository;

    /**
     * カルテ全取得
     * 
     * @return
     */
    public List<Karte> findAll() {
        return karteRepository.findByOrderByUpdatedAtDesc();
    }

    /**
     * 生徒IDでリストを取得
     * 
     * @return
     */
    public List<Karte> findAllByStudentId(String studentId) {
        return karteRepository.findAllByStudentIdOrderByDateDesc(studentId);
    }

    /**
     * カルテ1件取得
     * 
     * @param id
     * @return
     */
    public Karte findById(Long id) {
        return karteRepository.findById(id).orElseThrow();
    }

    /**
     * カルテ追加
     * 
     * @param karte
     * @return
     */
    public void add(String userId, KarteForm karteForm) {
        Karte karte = new Karte();
        karte.setStudentId(karteForm.getStudentId());
        karte.setDate(karteForm.getDate());
        karte.setContents(karteForm.getContents());
        karte.setCreatedBy(userId);
        karte.setUpdatedBy(userId);
        karteRepository.save(karte);
    }

    /**
     * カルテ更新
     * 
     * @param karte
     * @return
     */
    public void update(Long id, String userId, KarteForm karteForm) {
        Karte karte = karteRepository.findById(id).orElseThrow();
        karte.setDate(karteForm.getDate());
        karte.setContents(karteForm.getContents());
        karte.setUpdatedBy(userId);
        karteRepository.save(karte);
    }

    /**
     * カルテ削除
     * 
     * @param karte
     */
    public void deleteKarte(Long id) {
        Karte karte = karteRepository.findById(id).orElseThrow();
        karteRepository.delete(karte);
    }

//    /**
//     * カルテ削除
//     * 
//     * @param karte
//     */
//    public void deleteKarte(Long id) {
//        Karte karte = karteRepository.findById(id).orElseThrow();
//        karteRepository.delete(karte);
//    }

//    /**
//     * カルテCSVダウンロード用
//     * 
//     * @param karte
//     */
//  public void insertKarte(Karte karte) {
//      karteRepository.save(karte);
//  }

//  /**
//   * カルテ削除（理論削除）
//   * 
//   * @param karte
//   */
//  public void deleteKarte(Long karteId, Karte karte);

}

//package com.example.skarte.service;
//
//import java.util.List;
//
//import com.example.skarte.entity.Karte;
//import com.example.skarte.entity.StudentYear;
//
////インターフェイス
//public interface KarteService {
//
//  /**
//   * カルテ全取得
//   * 
//   * @return
//   */
//  public List<Karte> findAll();
//  
//  /**
//   * 生徒IDでリストを取得
//   * 
//   * @return
//   */
//  public List<Karte> findAllByStudentId(Long studentId);
//
//  /**
//   * カルテ1件取得
//   * 
//   * @param id
//   * @return
//   */
//  public Karte findById(Long id);
//
//  /**
//   * カルテ追加
//   * 
//   * @param karte
//   * @return
//   */
//  public void addKarte(Karte karte);
//
//  /**
//   * カルテ編集
//   * 
//   * @param karte
//   * @return
//   */
//  public Karte updateKarte(Long karteId, Karte karte);
//
//  /**
//   * カルテCSVダウンロード用
//   * 
//   * @param karte
//   */
//  public void insertKarte(Karte karte);
//
////  /**
////   * カルテ削除（理論削除）
////   * 
////   * @param karte
////   */
////  public void deleteKarte(Long karteId, Karte karte);
//
//}
