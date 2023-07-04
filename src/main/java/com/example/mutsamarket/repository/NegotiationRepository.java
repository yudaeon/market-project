package com.example.mutsamarket.repository;

import com.example.mutsamarket.entity.NegotiationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NegotiationRepository extends JpaRepository<NegotiationEntity, Long> {
    Boolean existsByItemId(Long itemId);
    Page<NegotiationEntity> findAllByItemId(Long itemId, Pageable pageable);
    List<NegotiationEntity> findAllByItemId(Long itemId);
    Boolean existsAllByItemIdAndWriterLikeAndPasswordLike(Long itemId, String writer, String password);
    Page<NegotiationEntity> findAllByItemIdAndWriterAndPasswordLike(Long itemId, String writer, String password, Pageable pageable);
}
