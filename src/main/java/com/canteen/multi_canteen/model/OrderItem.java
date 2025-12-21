package com.canteen.multi_canteen.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Parent Order (must not be null)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Order order;

    // ✅ Ordered menu item (must not be null)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    // ✅ Quantity must be >= 1
    @Column(nullable = false)
    private Integer quantity;

    // ✅ Price at time of order (fixes reporting & future price changes)
    @Column(name = "price_at_time", nullable = false)
    private Double priceAtTime;
}
