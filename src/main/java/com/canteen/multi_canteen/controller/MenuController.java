package com.canteen.multi_canteen.controller;

import com.canteen.multi_canteen.model.MenuItem;
import com.canteen.multi_canteen.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*")
public class MenuController {

    private final MenuService menuService;

    // ✅ Constructor injection (prevents null issues)
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // ✅ GET /api/menu/{collegeId}
    @GetMapping("/{collegeId}")
    public List<MenuItem> getMenuByCollege(@PathVariable Long collegeId) {
        return menuService.getMenuByCollegeId(collegeId);
    }

    // ✅ POST /api/menu/{collegeId} (Create)
    @PostMapping("/{collegeId}")
    public ResponseEntity<?> createMenuItem(@PathVariable Long collegeId,
                                            @RequestBody MenuItem menuItem) {
        try {
            MenuItem saved = menuService.createMenuItem(collegeId, menuItem);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    // ✅ PUT /api/menu/{itemId} (Update)
    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateMenuItem(@PathVariable Long itemId,
                                            @RequestBody MenuItem menuItem) {
        try {
            MenuItem updated = menuService.updateMenuItem(itemId, menuItem);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    // ✅ DELETE /api/menu/{itemId} (Delete)
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long itemId) {
        try {
            menuService.deleteMenuItem(itemId);
            Map<String, String> res = new HashMap<>();
            res.put("message", "Menu item deleted");
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }
}
