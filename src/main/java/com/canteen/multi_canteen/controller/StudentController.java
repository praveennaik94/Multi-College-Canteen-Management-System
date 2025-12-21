package com.canteen.multi_canteen.controller;

import com.canteen.multi_canteen.model.*;
import com.canteen.multi_canteen.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final CollegeRepository collegeRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public StudentController(CollegeRepository collegeRepository,
                             MenuItemRepository menuItemRepository,
                             OrderRepository orderRepository,
                             OrderItemRepository orderItemRepository) {
        this.collegeRepository = collegeRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // ✅ Student Home
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        model.addAttribute("user", loggedUser);
        model.addAttribute("colleges", collegeRepository.findAll());
        model.addAttribute("selectedCollegeId", null);
        model.addAttribute("selectedCategory", null);
        model.addAttribute("menuItems", null);

        // ✅ Read error message from session (important)
        model.addAttribute("errorMessage", session.getAttribute("errorMessage"));
        session.removeAttribute("errorMessage");

        model.addAttribute("orders", null);
        model.addAttribute("orderItemsNames", new HashMap<Long, String>());
        model.addAttribute("orderTotals", new HashMap<Long, Double>());

        return "index";
    }

    // ✅ View Menu (college + category mandatory)
    @GetMapping("/menu")
    public String viewMenu(@RequestParam(value = "collegeId", required = false) Long collegeId,
                           @RequestParam(value = "category", required = false) String category,
                           HttpSession session,
                           Model model) {

        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        model.addAttribute("user", loggedUser);
        model.addAttribute("colleges", collegeRepository.findAll());
        model.addAttribute("selectedCollegeId", collegeId);
        model.addAttribute("selectedCategory", category);

        if (collegeId == null || category == null || category.isBlank()) {
            model.addAttribute("menuItems", null);
            model.addAttribute("errorMessage", "Please select both a college and a category.");
            return "index";
        }

        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new RuntimeException("College not found"));

        // ✅ IMPORTANT: filter by college + category
        List<MenuItem> menuItems =
                menuItemRepository.findByCollegeAndCategoryIgnoreCase(college, category);

        model.addAttribute("menuItems", menuItems);
        model.addAttribute("errorMessage", null);

        return "index";
    }

    // ✅ Place Order (FIXED: totalAmount + empty selection)
    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam(name = "selectedItems", required = false) List<Long> selectedItems,
                             HttpSession session) {

        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        if (selectedItems == null || selectedItems.isEmpty()) {
            session.setAttribute("errorMessage", "Please select at least one item before placing order.");
            return "redirect:/student/home";
        }

        MenuItem firstItem = menuItemRepository.findById(selectedItems.get(0))
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        College college = firstItem.getCollege();

        // ✅ Calculate total FIRST (critical fix)
        double totalAmount = 0.0;
        for (Long itemId : selectedItems) {
            MenuItem item = menuItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));
            totalAmount += item.getPrice();
        }

        Order order = Order.builder()
                .student(loggedUser)
                .college(college)
                .status("Pending")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalAmount(totalAmount)   // ✅ NEVER NULL now
                .build();

        orderRepository.save(order);

        // ✅ Save order items + decrease stock
        for (Long itemId : selectedItems) {
            MenuItem item = menuItemRepository.findById(itemId).orElseThrow();

            if (item.getAvailableQuantity() > 0) {
                item.setAvailableQuantity(item.getAvailableQuantity() - 1);
                menuItemRepository.save(item);

                OrderItem orderItem = OrderItem.builder()
                        .order(order)
                        .menuItem(item)
                        .quantity(1)
                        .priceAtTime(item.getPrice())
                        .build();

                orderItemRepository.save(orderItem);
            }
        }

        return "redirect:/student/orders";
    }

    // ✅ My Orders
    @GetMapping("/orders")
    public String myOrders(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        model.addAttribute("user", loggedUser);
        model.addAttribute("colleges", collegeRepository.findAll());
        model.addAttribute("selectedCollegeId", null);
        model.addAttribute("selectedCategory", null);

        List<Order> orders = orderRepository.findByStudent(loggedUser);

        Map<Long, String> orderItemsNames = new HashMap<>();
        Map<Long, Double> orderTotals = new HashMap<>();

        for (Order order : orders) {
            List<OrderItem> items = orderItemRepository.findByOrder(order);
            List<String> names = new ArrayList<>();
            double total = 0.0;

            for (OrderItem oi : items) {
                if (oi.getMenuItem() != null) {
                    names.add(oi.getMenuItem().getName());
                    total += oi.getMenuItem().getPrice();
                }
            }

            orderItemsNames.put(order.getId(), String.join(", ", names));
            orderTotals.put(order.getId(), total);
        }

        model.addAttribute("orders", orders);
        model.addAttribute("orderItemsNames", orderItemsNames);
        model.addAttribute("orderTotals", orderTotals);

        return "index";
    }
}
