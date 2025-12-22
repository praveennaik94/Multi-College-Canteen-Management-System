package com.canteen.multi_canteen.controller;

import com.canteen.multi_canteen.model.College;
import com.canteen.multi_canteen.service.CollegeService;
import com.canteen.multi_canteen.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/colleges")
@CrossOrigin(origins = "*")   // keep for frontend/API usage
public class CollegeController {

    private final CollegeService collegeService;
    private final OrderService orderService;

    //Constructor injection (recommended, avoids null issues)
    public CollegeController(CollegeService collegeService,
                             OrderService orderService) {
        this.collegeService = collegeService;
        this.orderService = orderService;
    }

    //  1. Get all colleges
    @GetMapping
    public List<College> getAllColleges() {
        return collegeService.getAllColleges();
    }

    // 2. Get today's sales report for a college
    @GetMapping("/{collegeId}/reports/today")
    public Map<String, Object> getTodayReport(@PathVariable Long collegeId) {
        return orderService.getTodayReportForCollege(collegeId);
    }
}
