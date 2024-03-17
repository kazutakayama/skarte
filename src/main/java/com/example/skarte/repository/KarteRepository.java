package com.example.skarte.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.skarte.entity.Karte;

@Repository
public interface KarteRepository extends JpaRepository<Karte, Long> {
    
    //更新日時の降順で未削除の投稿（削除済みは表示しない）　
    List<Karte> findByDeletedFalseOrderByUpdatedAtDesc();
    
    //IDで検索する
    public Optional<Karte> findById(Long id);
}