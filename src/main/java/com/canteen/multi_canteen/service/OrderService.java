package com.canteen.multi_canteen.service;

import com.canteen.multi_canteen.dto.CreateOrderRequest;
import com.canteen.multi_canteen.dto.OrderItemRequest;
import com.canteen.multi_canteen.dto.UpdateOrderStatusRequest;
import com.canteen.multi_canteen.model.*;
import com.canteen.multi_canteen.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // ✅ 1. Student places order
    public Order placeOrder(CreateOrderRequest request) {

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        College college = collegeRepository.findById(request.getCollegeId())
                .orElseThrow(() -> new RuntimeException("College not found"));

        // ✅ Create order
        Order order = new Order();
        order.setStudent(student);
        order.setCollege(college);
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequest itemReq : request.getItems()) {

            MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                    .orElseThrow(() ->
                            new RuntimeException("Menu item not found: " + itemReq.getMenuItemId()));

            // ✅ Quantity safety
            int qty = itemReq.getQuantity() != null ? itemReq.getQuantity() : 1;

            if (menuItem.getAvailableQuantity() < qty) {
                throw new RuntimeException("Insufficient stock for " + menuItem.getName());
            }

            // ✅ Calculate
            double price = menuItem.getPrice();
            totalAmount += price * qty;

            // ✅ Reduce stock
            menuItem.setAvailableQuantity(menuItem.getAvailableQuantity() - qty);
            menuItemRepository.save(menuItem);

            // ✅ Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(qty);
            orderItem.setPriceAtTime(price);

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setItems(orderItems); // ✅ IMPORTANT (cascade works)

        // ✅ Save order (OrderItems saved via cascade)
        return orderRepository.save(order);
    }

    // ✅ 2. Student: get my orders
    public List<Order> getOrdersForStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return orderRepository.findByStudent(student);
    }

    // ✅ 3. Admin: get orders for a college
    public List<Order> getOrdersForCollege(Long collegeId) {
        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new RuntimeException("College not found"));
        return orderRepository.findByCollege(college);
    }

    // ✅ 4. Admin: update order status
    public Order updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(request.getStatus());
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    // ✅ 5. Report: today's summary for a college
    public Map<String, Object> getTodayReportForCollege(Long collegeId) {

        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new RuntimeException("College not found"));

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);

        List<Order> orders =
                orderRepository.findByCollegeAndCreatedAtBetween(college, start, end);

        int totalOrders = orders.size();
        double totalRevenue = 0.0;
        int completedOrders = 0;
        int pendingOrders = 0;

        for (Order o : orders) {
            totalRevenue += o.getTotalAmount();

            if ("COMPLETED".equalsIgnoreCase(o.getStatus())) {
                completedOrders++;
            } else {
                pendingOrders++;
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("collegeId", collegeId);
        report.put("date", today.toString());
        report.put("totalOrders", totalOrders);
        report.put("totalRevenue", totalRevenue);
        report.put("completedOrders", completedOrders);
        report.put("pendingOrders", pendingOrders);

        return report;
    }
}
