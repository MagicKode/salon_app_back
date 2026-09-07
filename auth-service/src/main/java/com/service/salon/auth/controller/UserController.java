package com.service.salon.auth.controller;

import com.service.salon.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/name/{phone}")
    public ResponseEntity<String> getUserByName(@PathVariable String phone) {
        String name = userRepository.findNameByPhone(phone).orElse("Клиент");
        return ResponseEntity.ok(name);
    }
}
