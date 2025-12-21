package com.canteen.multi_canteen.service;

import com.canteen.multi_canteen.model.College;
import com.canteen.multi_canteen.model.MenuItem;
import com.canteen.multi_canteen.repository.CollegeRepository;
import com.canteen.multi_canteen.repository.MenuItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    // ✅ Get all menu items for a specific college
    public List<MenuItem> getMenuByCollegeId(Long collegeId) {
        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new RuntimeException("College not found"));
        return menuItemRepository.findByCollege(college);
    }

    // ✅ Create a new menu item
    public MenuItem createMenuItem(Long collegeId, MenuItem menuItem) {

        if (menuItem.getName() == null || menuItem.getName().isBlank()) {
            throw new RuntimeException("Menu item name is required");
        }

        if (menuItem.getPrice() == null || menuItem.getPrice() <= 0) {
            throw new RuntimeException("Menu item price must be greater than 0");
        }

        if (menuItem.getCategory() == null || menuItem.getCategory().isBlank()) {
            throw new RuntimeException("Menu item category is required");
        }

        if (menuItem.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Available quantity must be greater than 0");
        }

        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new RuntimeException("College not found"));

        // ✅ REQUIRED: set college properly (fixes FK error)
        menuItem.setCollege(college);

        return menuItemRepository.save(menuItem);
    }

    // ✅ Update existing menu item
    public MenuItem updateMenuItem(Long itemId, MenuItem updated) {
        MenuItem existing = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        if (updated.getName() != null && !updated.getName().isBlank()) {
            existing.setName(updated.getName());
        }

        if (updated.getPrice() != null && updated.getPrice() > 0) {
            existing.setPrice(updated.getPrice());
        }

        if (updated.getCategory() != null && !updated.getCategory().isBlank()) {
            existing.setCategory(updated.getCategory());
        }

        if (updated.getAvailableQuantity() >= 0) {
            existing.setAvailableQuantity(updated.getAvailableQuantity());
        }

        return menuItemRepository.save(existing);
    }

    // ✅ Delete menu item
    public void deleteMenuItem(Long itemId) {
        if (!menuItemRepository.existsById(itemId)) {
            throw new RuntimeException("Menu item not found");
        }

        menuItemRepository.deleteById(itemId);
    }
}
