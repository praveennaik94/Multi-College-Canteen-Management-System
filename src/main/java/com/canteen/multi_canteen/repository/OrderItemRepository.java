package com.canteen.multi_canteen.repository;

import com.canteen.multi_canteen.model.OrderItem;
import com.canteen.multi_canteen.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}
