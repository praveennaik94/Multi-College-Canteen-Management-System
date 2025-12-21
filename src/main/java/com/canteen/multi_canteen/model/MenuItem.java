package com.canteen.multi_canteen.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    // Breakfast / Snacks / Chinese / Biryani / Drinks
    @Column(nullable = false)
    private String category;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "college_id", nullable = false)
    private College college;
}
