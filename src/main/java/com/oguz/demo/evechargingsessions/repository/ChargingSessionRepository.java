package com.oguz.demo.evechargingsessions.repository;

import com.oguz.demo.evechargingsessions.entity.ChargingSession;

import java.util.List;

public interface ChargingSessionRepository {

    void put(ChargingSession chargingSession);
    ChargingSession get(String id);
    List<ChargingSession> getAll();

}
