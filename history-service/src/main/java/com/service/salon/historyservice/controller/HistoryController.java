package com.service.salon.historyservice.controller;

import com.service.salon.historyservice.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/active")
    public ResponseEntity<?> getActiveHistory(
            @RequestHeader("Authorization") String token,
            @RequestHeader("X-User-Name") String userPhone) {
        return ResponseEntity.ok(historyService.getActiveHistory(token, userPhone));
    }

    @GetMapping("/past")
    public ResponseEntity<?> getPastHistory(
            @RequestHeader("Authorization") String token,
            @RequestHeader("X-User-Name") String userPhone) {
        return ResponseEntity.ok(historyService.getPastHistory(token, userPhone));
    }
}
