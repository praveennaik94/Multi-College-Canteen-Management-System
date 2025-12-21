package com.canteen.multi_canteen.controller;

import com.canteen.multi_canteen.model.*;
import com.canteen.multi_canteen.repository.*;
import com.canteen.multi_canteen.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CollegeRepository collegeRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public AdminController(CollegeRepository collegeRepository,
                           MenuItemRepository menuItemRepository,
                           OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository,
                           UserRepository userRepository,
                           OrderService orderService) {
        this.collegeRepository = collegeRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    // ✅ Admin Dashboard
    @GetMapping("")
    public String adminDashboard(HttpSession session, Model model) {

        User admin = (User) session.getAttribute("loggedUser");
        if (admin == null || !"ADMIN".equalsIgnoreCase(admin.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("user", admin);

        // 1️⃣ Colleges (always from DB)
        List<College> colleges = collegeRepository.findAll();
        model.addAttribute("colleges", colleges);

        // 2️⃣ Menu Items
        List<MenuItem> menuItems = menuItemRepository.findAll();
        model.addAttribute("menuItems", menuItems);

        // 3️⃣ Orders + totals (SAFE calculation)
        List<Order> orders = orderRepository.findAll();
        Map<Long, String> orderItemsNames = new HashMap<>();
        Map<Long, Double> orderTotals = new HashMap<>();

        double totalRevenueAllOrders = 0.0;

        for (Order order : orders) {
            List<OrderItem> items = orderItemRepository.findByOrder(order);

            List<String> names = new ArrayList<>();
            double total = 0.0;

            for (OrderItem oi : items) {
                if (oi.getMenuItem() != null) {
                    names.add(oi.getMenuItem().getName());
                    total += oi.getPriceAtTime();   // ✅ FIX: use priceAtTime
                }
            }

            orderItemsNames.put(order.getId(), String.join(", ", names));
            orderTotals.put(order.getId(), total);
            totalRevenueAllOrders += total;
        }

        model.addAttribute("orders", orders);
        model.addAttribute("orderItemsNames", orderItemsNames);
        model.addAttribute("orderTotals", orderTotals);
        model.addAttribute("totalRevenueAllOrders", totalRevenueAllOrders);

        // 4️⃣ Students
        List<User> students = userRepository.findAll()
                .stream()
                .filter(u -> "STUDENT".equalsIgnoreCase(u.getRole()))
                .toList();
        model.addAttribute("students", students);

        // 5️⃣ Today Sales Reports
        List<Map<String, Object>> todayReports = new ArrayList<>();
        int grandTotalOrders = 0;
        double grandRevenue = 0.0;
        int grandCompleted = 0;
        int grandPending = 0;

        for (College c : colleges) {
            Map<String, Object> report = orderService.getTodayReportForCollege(c.getId());
            report.put("collegeName", c.getName());
            todayReports.add(report);

            grandTotalOrders += ((Number) report.get("totalOrders")).intValue();
            grandRevenue += ((Number) report.get("totalRevenue")).doubleValue();
            grandCompleted += ((Number) report.get("completedOrders")).intValue();
            grandPending += ((Number) report.get("pendingOrders")).intValue();
        }

        model.addAttribute("todayReports", todayReports);
        model.addAttribute("grandTotalOrders", grandTotalOrders);
        model.addAttribute("grandRevenue", grandRevenue);
        model.addAttribute("grandCompletedOrders", grandCompleted);
        model.addAttribute("grandPendingOrders", grandPending);

        return "admin";
    }

    // ✅ Add College (NO duplicate IDs, saved in DB correctly)
    @PostMapping("/addCollege")
    public String addCollege(@RequestParam String name,
                             @RequestParam String location) {

        // ✅ Prevent duplicate college names
        Optional<College> existing = collegeRepository.findByNameIgnoreCase(name);
        if (existing.isPresent()) {
            return "redirect:/admin";
        }

        College college = new College();
        college.setName(name);
        college.setLocation(location);

        collegeRepository.save(college);
        return "redirect:/admin";
    }

    // ✅ Delete College (CASCADE SAFE)
    @PostMapping("/deleteCollege/{id}")
    public String deleteCollege(@PathVariable Long id) {
        collegeRepository.deleteById(id);
        return "redirect:/admin";
    }

    // ✅ Add Menu Item (FK ERROR FIXED)
    @PostMapping("/addMenu")
    public String addMenuItem(@RequestParam Long collegeId,
                              @RequestParam String name,
                              @RequestParam String category,
                              @RequestParam double price,
                              @RequestParam int availableQuantity) {

        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new RuntimeException("College not found"));

        MenuItem menuItem = new MenuItem();
        menuItem.setCollege(college);      // ✅ VERY IMPORTANT
        menuItem.setName(name);
        menuItem.setCategory(category);
        menuItem.setPrice(price);
        menuItem.setAvailableQuantity(availableQuantity);

        menuItemRepository.save(menuItem);
        return "redirect:/admin";
    }

    // ✅ Delete Menu Item
    @PostMapping("/deleteMenu/{id}")
    public String deleteMenuItem(@PathVariable Long id) {
        menuItemRepository.deleteById(id);
        return "redirect:/admin";
    }

    // ✅ Update Order Status
    @PostMapping("/updateStatus")
    public String updateOrderStatus(@RequestParam Long orderId,
                                    @RequestParam String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);

        return "redirect:/admin";
    }
}
