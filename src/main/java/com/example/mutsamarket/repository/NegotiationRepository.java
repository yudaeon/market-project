package com.example.mutsamarket.repository;

import com.example.mutsamarket.entity.NegotiationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NegotiationRepository extends JpaRepository<NegotiationEntity, Long> {
    List<NegotiationEntity> findByItemId(Long itemId);
}
