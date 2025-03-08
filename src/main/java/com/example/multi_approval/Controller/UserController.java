package com.example.multi_approval.Controller;

import com.example.multi_approval.DTO.UserRequestDto;
import com.example.multi_approval.Entity.Users;
import com.example.multi_approval.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserRequestDto userDto) {
        return userService.signUp(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String loginId) {
        return userService.logIn(loginId);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String loginId) {
        return userService.logOut(loginId);
    }
}
