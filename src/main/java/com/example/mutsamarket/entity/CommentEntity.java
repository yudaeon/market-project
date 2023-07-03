package com.example.mutsamarket.entity;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(nullable = false)
    private String writer;

    private String password;

    private String content;

    private String reply;
}
