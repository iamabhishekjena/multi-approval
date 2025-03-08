package com.example.multi_approval.Service;

import com.example.multi_approval.Constants.Constants;
import com.example.multi_approval.DTO.UserRequestDto;
import com.example.multi_approval.Entity.Users;
import com.example.multi_approval.Repository.UserRepository;
import com.example.multi_approval.Utility.Utility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final int loginLength = 8;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> signUp(UserRequestDto userDto) {
        if (userRepository.existsByEmailId(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("User already registered.");
        }

        Users user = new Users();
        user.setName(userDto.getName());
        user.setEmailId(userDto.getEmail());
        user.setLoginId(Utility.generateLoginId(Constants.PREFIX_USER.getValue(), loginLength));

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully with Login ID: " + user.getLoginId());
    }

    public ResponseEntity<String> logIn(String loginId) {
        Optional<Users> existingUser = userRepository.findByLoginId(loginId);
        if (existingUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User Not Found");
        }
        Users user = existingUser.get();
        if ("Y".equals(user.getLoginStatus())) {
            return ResponseEntity.badRequest().body("User already logged in");
        }
        user.setLoginStatus("Y");
        userRepository.save(user);
        return ResponseEntity.ok("User Login Successful");
    }

    public ResponseEntity<String> logOut(String loginId) {
        Optional<Users> existingUser = userRepository.findByLoginId(loginId);
        if (existingUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User Not Found");
        }
        Users user = existingUser.get();
        if ("N".equals(user.getLoginStatus())) {
            return ResponseEntity.badRequest().body("User already logged out");
        }
        user.setLoginStatus("N");
        userRepository.save(user);
        return ResponseEntity.ok("User Logout Successful");
    }

    public boolean checkUserLoginStatus(int userId) {
        return userRepository.findById(userId)
                .map(user -> "Y".equals(user.getLoginStatus()))
                .orElse(false);
    }

    public int getUseridByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .map(Users::getUserId)
                .orElse(-1);
    }

    public Users getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElse(null);
    }
}
