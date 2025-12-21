package com.canteen.multi_canteen.repository;

import com.canteen.multi_canteen.model.College;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollegeRepository extends JpaRepository<College, Long> {

    // ✅ Case-insensitive check to prevent duplicate colleges
    Optional<College> findByNameIgnoreCase(String name);
}
