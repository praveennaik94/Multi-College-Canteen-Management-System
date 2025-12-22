package com.canteen.multi_canteen.controller;

import com.canteen.multi_canteen.model.College;
import com.canteen.multi_canteen.model.User;
import com.canteen.multi_canteen.repository.CollegeRepository;
import com.canteen.multi_canteen.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final CollegeRepository collegeRepository;

    public AuthController(UserRepository userRepository,
                          CollegeRepository collegeRepository) {
        this.userRepository = userRepository;
        this.collegeRepository = collegeRepository;
    }

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/login";
    }

    // ================= LOGIN =================
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "User not found. Please register.");
            return "login";
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid password!");
            return "login";
        }

        // Save logged-in user
        session.setAttribute("loggedUser", user);

        //  Safe role handling
        String role = user.getRole() == null ? "" : user.getRole().trim().toUpperCase();

        if ("ADMIN".equals(role)) {
            return "redirect:/admin";
        }

        return "redirect:/student/home";
    }

    // ================= REGISTER (STUDENT ONLY) =================
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String name,
                             @RequestParam String mobile,
                             @RequestParam String collegeName,
                             @RequestParam String gender,
                             @RequestParam String email,
                             @RequestParam String password,
                             Model model) {

        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already registered. Please login.");
            return "register";
        }

        // Create college if not exists
        collegeRepository.findByNameIgnoreCase(collegeName)
                .orElseGet(() -> {
                    College c = new College();
                    c.setName(collegeName);
                    c.setLocation(null);
                    return collegeRepository.save(c);
                });

        User user = User.builder()
                .name(name)
                .mobile(mobile)
                .collegeName(collegeName)
                .gender(gender)
                .email(email)
                .password(password)
                .role("STUDENT")  
                .build();

        userRepository.save(user);

        model.addAttribute("success", "Registration successful! Please login.");
        return "login";
    }

    // ================= LOGOUT =================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
