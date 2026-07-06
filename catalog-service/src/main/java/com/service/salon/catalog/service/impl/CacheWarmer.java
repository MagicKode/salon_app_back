package com.service.salon.catalog.service.impl;

import com.service.salon.catalog.service.CatalogService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
@Slf4j
public class CacheWarmer {

    private final CatalogService catalogService;

    @PostConstruct
    public void warmUpSalonCache() {
        try {
            catalogService.getMainSalon();   // вызовет загрузку из БД и положит в Redis
            log.info("✅ Cache 'salon' warmed up successfully");
        } catch (Exception e) {
            log.warn("⚠️ Could not warm up salon cache: {}", e.getMessage());
        }
    }
}
