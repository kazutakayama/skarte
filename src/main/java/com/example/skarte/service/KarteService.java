package com.example.skarte.service;

import java.util.List;

import com.example.skarte.entity.Karte;

//インターフェイス
public interface KarteService {

  /**
   * カルテ全取得
   * 
   * @return
   */
  public List<Karte> findAll();

  /**
   * カルテ1件取得
   * 
   * @param id
   * @return
   */
  public Karte findById(Long id);

  /**
   * カルテ追加
   * 
   * @param karte
   * @return
   */
  public void addKarte(Karte karte);

  /**
   * カルテ編集
   * 
   * @param karte
   * @return
   */
  public Karte updateKarte(Long karteId, Karte karte);

  /**
   * カルテCSVダウンロード用
   * 
   * @param karte
   */
  public void insertKarte(Karte karte);

  /**
   * カルテ削除（理論削除）
   * 
   * @param karte
   */
  public void deleteKarte(Long karteId, Karte karte);

}
