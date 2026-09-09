package com.service.salon.catalog.service;

import com.service.salon.catalog.model.dto.CreateMasterRequest;
import com.service.salon.catalog.model.dto.MasterDto;

import java.util.List;

public interface MasterService {
    List<MasterDto> getActiveMasters();

    MasterDto getMasterById(Long id);

    MasterDto createMaster(CreateMasterRequest request);

    MasterDto updateMaster(Long id, CreateMasterRequest request);

    void deleteMaster(Long id);

    MasterDto toggleActive(Long id);
}
