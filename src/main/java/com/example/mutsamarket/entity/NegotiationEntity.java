package com.example.mutsamarket.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "negotiation")
public class NegotiationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(nullable = false)
    private Long suggestedPrice;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String password;
}
