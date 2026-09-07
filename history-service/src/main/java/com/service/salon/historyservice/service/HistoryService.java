package com.service.salon.historyservice.service;

import com.service.salon.historyservice.model.Booking;

import java.util.List;

public interface HistoryService {
    List<Booking> getActiveHistory(String token, String userPhone);

    List<Booking> getPastHistory(String token, String userPhone);
}
