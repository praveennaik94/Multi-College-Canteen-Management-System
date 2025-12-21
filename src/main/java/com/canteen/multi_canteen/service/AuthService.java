package com.canteen.multi_canteen.service;

import com.canteen.multi_canteen.dto.LoginRequest;
import com.canteen.multi_canteen.dto.RegisterRequest;
import com.canteen.multi_canteen.model.College;
import com.canteen.multi_canteen.model.User;
import com.canteen.multi_canteen.repository.CollegeRepository;
import com.canteen.multi_canteen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    // ✅ STUDENT registration only
    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already registered!";
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role("STUDENT")
                .collegeName(request.getCollegeName()) // ✅ STRING, not ID
                .build();

        userRepository.save(user);
        return "Student registered successfully!";
    }

    // ✅ API-based login (UI uses session login)
    public String login(LoginRequest request) {

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return "Invalid email!";
        }

        User user = userOpt.get();

        if (!user.getPassword().equals(request.getPassword())) {
            return "Incorrect password!";
        }

        return "Login successful as " + user.getRole();
    }

    // ✅ Create default admin once (call on startup if needed)
    public void createDefaultAdmin() {

        if (userRepository.findByEmail("admin@canteen.com").isEmpty()) {
            User admin = User.builder()
                    .name("Admin User")
                    .email("admin@canteen.com")
                    .password("admin123")
                    .role("ADMIN")
                    .build();

            userRepository.save(admin);
        }
    }
}
