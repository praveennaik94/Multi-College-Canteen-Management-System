package com.canteen.multi_canteen.repository;

import com.canteen.multi_canteen.model.Order;
import com.canteen.multi_canteen.model.College;
import com.canteen.multi_canteen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ✅ Student order history
    List<Order> findByStudent(User student);

    // ✅ Orders by college
    List<Order> findByCollege(College college);

    // ✅ Sales report (date-wise per college)
    List<Order> findByCollegeAndCreatedAtBetween(
            College college,
            LocalDateTime start,
            LocalDateTime end
    );
}
