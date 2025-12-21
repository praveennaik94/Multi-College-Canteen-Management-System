package com.canteen.multi_canteen.repository;

import com.canteen.multi_canteen.model.MenuItem;
import com.canteen.multi_canteen.model.College;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    // ✅ Get menu items by college
    List<MenuItem> findByCollege(College college);

    // ✅ Filter by college + category
    List<MenuItem> findByCollegeAndCategoryIgnoreCase(College college, String category);

}
