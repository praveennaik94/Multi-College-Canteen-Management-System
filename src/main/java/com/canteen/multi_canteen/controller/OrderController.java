package com.canteen.multi_canteen.controller;

import com.canteen.multi_canteen.dto.CreateOrderRequest;
import com.canteen.multi_canteen.dto.UpdateOrderStatusRequest;
import com.canteen.multi_canteen.model.Order;
import com.canteen.multi_canteen.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    //  Constructor injection (recommended)
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //  POST /api/orders  (student places order)
    @PostMapping
    public Map<String, Object> placeOrder(@RequestBody CreateOrderRequest request) {

        Order savedOrder = orderService.placeOrder(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order placed successfully");
        response.put("orderId", savedOrder.getId());
        response.put("totalAmount", savedOrder.getTotalAmount());
        response.put("status", savedOrder.getStatus());

        return response;
    }

    //  GET /api/orders/my?studentId=1  (student's orders - compact)
    @GetMapping("/my")
    public List<Map<String, Object>> getMyOrders(@RequestParam Long studentId) {

        List<Order> orders = orderService.getOrdersForStudent(studentId);

        return orders.stream()
                .map(o -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("orderId", o.getId());
                    m.put("status", o.getStatus());
                    m.put("totalAmount", o.getTotalAmount());
                    m.put("collegeId", o.getCollege().getId());
                    m.put("itemCount", o.getItems() != null ? o.getItems().size() : 0);
                    m.put("createdAt", o.getCreatedAt());
                    return m;
                })
                .collect(Collectors.toList());
    }

    //  GET /api/orders/college/{collegeId}  (admin view - compact)
    @GetMapping("/college/{collegeId}")
    public List<Map<String, Object>> getOrdersForCollege(@PathVariable Long collegeId) {

        List<Order> orders = orderService.getOrdersForCollege(collegeId);

        return orders.stream()
                .map(o -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("orderId", o.getId());
                    m.put("studentId", o.getStudent().getId());
                    m.put("status", o.getStatus());
                    m.put("totalAmount", o.getTotalAmount());
                    m.put("itemCount", o.getItems() != null ? o.getItems().size() : 0);
                    m.put("createdAt", o.getCreatedAt());
                    return m;
                })
                .collect(Collectors.toList());
    }

    //  PATCH /api/orders/{orderId}/status  (admin updates status)
    @PatchMapping("/{orderId}/status")
    public Map<String, Object> updateOrderStatus(@PathVariable Long orderId,
                                                 @RequestBody UpdateOrderStatusRequest request) {

        Order updated = orderService.updateOrderStatus(orderId, request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order status updated successfully");
        response.put("orderId", updated.getId());
        response.put("status", updated.getStatus());
        response.put("totalAmount", updated.getTotalAmount());

        return response;
    }
}
