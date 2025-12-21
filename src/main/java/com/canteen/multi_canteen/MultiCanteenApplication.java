package com.canteen.multi_canteen;

import com.canteen.multi_canteen.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultiCanteenApplication implements CommandLineRunner {

    @Autowired
    private AuthService authService;

    public static void main(String[] args) {
        SpringApplication.run(MultiCanteenApplication.class, args);
    }

    @Override
    public void run(String... args) {
        authService.createDefaultAdmin();
    }
}
