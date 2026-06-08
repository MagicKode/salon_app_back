package com.service.salon.booking.controller;

import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.dto.DailyScheduleDto;
import com.service.salon.booking.model.dto.DayStatDto;
import com.service.salon.booking.service.MasterScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/master/schedule")
@RequiredArgsConstructor
public class MasterScheduleController {
    private final MasterScheduleService masterScheduleService;

    @GetMapping("/today")
    public ResponseEntity<DailyScheduleDto> getTodaySchedule(@RequestHeader("X-User-Name") String username) {
        return ResponseEntity.ok(masterScheduleService.getTodaySchedule(username));
    }

    @GetMapping("/month")
    public ResponseEntity<List<DayStatDto>> getMonthlyStats(
            @RequestHeader("X-User-Name") String username,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(masterScheduleService.getMonthlyStats(username, month, year));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Booking>> getMonthAppointments(
            @RequestHeader("X-User-Name") String username,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(masterScheduleService.getMonthAppointments(username, month, year));
    }
}
