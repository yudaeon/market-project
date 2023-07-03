package com.example.mutsamarket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sales_item")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //null 허용 x
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private String imageUrl;

    @Column(nullable = false)
    private Integer minPriceWanted;

    private String status;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String password;
}