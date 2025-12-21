package com.canteen.multi_canteen.repository;

import com.canteen.multi_canteen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ Used for login authentication
    Optional<User> findByEmail(String email);
}
