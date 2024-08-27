package com.example.application;

import com.example.application.data.Role;
import com.example.application.data.User;
import com.example.application.service.UserManagementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabasePopulator implements CommandLineRunner {

    UserManagementService userService;

    public DatabasePopulator(UserManagementService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        // Creamos admin
        if (userService.count() == 0) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("admin");
            user.setEmail("admin@uca.es");
            user.addRole(Role.ADMIN);
            userService.registerUser(user);
            userService.activateUser(user.getEmail(), user.getRegisterCode());
            System.out.println("Admin created");

        }


    }


}
