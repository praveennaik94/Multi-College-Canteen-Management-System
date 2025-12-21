package com.canteen.multi_canteen.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Student who placed the order
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // ✅ College where order is placed
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    // Pending / Preparing / Completed / Cancelled
    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ✅ MUST never be null (fixes your 500 error)
    @Column(name = "total_amount", nullable = false)
    @Builder.Default
    private Double totalAmount = 0.0;

    // ✅ Order items
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
}
