package com.canteen.multi_canteen.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colleges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    // ✅ When a college is deleted → delete its menu items
    @Builder.Default
    @OneToMany(
            mappedBy = "college",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MenuItem> menuItems = new ArrayList<>();

    // ✅ When a college is deleted → delete its orders
    @Builder.Default
    @OneToMany(
            mappedBy = "college",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Order> orders = new ArrayList<>();
}
