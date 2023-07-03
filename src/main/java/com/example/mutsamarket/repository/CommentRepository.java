package com.example.mutsamarket.repository;

import com.example.mutsamarket.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
