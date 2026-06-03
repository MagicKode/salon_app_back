package com.service.salon.historyservice.service;

import com.service.salon.historyservice.model.Booking;

import java.util.List;

public interface HistoryService {
    List<Booking> getClientHistory(String token, String userPhone);
}
