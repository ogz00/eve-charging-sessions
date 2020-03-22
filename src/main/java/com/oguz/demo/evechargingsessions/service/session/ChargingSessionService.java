package com.oguz.demo.evechargingsessions.service.session;

import com.oguz.demo.evechargingsessions.dto.request.IdentityRequest;
import com.oguz.demo.evechargingsessions.dto.response.ChargingSessionDto;

import java.util.List;

public interface ChargingSessionService {
    ChargingSessionDto start(IdentityRequest req);
    ChargingSessionDto stop(String id);
    List<ChargingSessionDto> getAll();

}
