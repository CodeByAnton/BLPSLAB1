package com.blpsteam.blpslab1.data.entities.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Boolean approved;

//    @ManyToOne
//    @JoinColumn(name = "seller_id", nullable = false)
//    private User seller;
    @Column(nullable = false, name = "seller_id")
    private Long sellerId;
}
