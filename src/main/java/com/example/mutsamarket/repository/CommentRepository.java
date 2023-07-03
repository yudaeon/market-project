package com.example.mutsamarket.repository;

import com.example.mutsamarket.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findByItemId(Long itemId, Pageable pageable);
    Optional<CommentEntity> findByIdAndItemId(Long id, Long itemId);
}
