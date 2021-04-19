package com.myowncompany.account.manager.controller;

import com.myowncompany.account.manager.domain.User;
import com.myowncompany.account.manager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/account-manager/user")
public class UserController {
    private final UserService userService;

    @PutMapping("/add")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/")
    public List<User> getAll(){
        return userService.getAll();
    }

}
