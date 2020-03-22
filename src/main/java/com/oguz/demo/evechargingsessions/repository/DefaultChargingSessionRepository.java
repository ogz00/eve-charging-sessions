package com.oguz.demo.evechargingsessions.repository;

import com.oguz.demo.evechargingsessions.entity.ChargingSession;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DefaultChargingSessionRepository implements ChargingSessionRepository {

    private Map<String, ChargingSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void put(ChargingSession chargingSession) {
        sessionMap.put(chargingSession.getId(), chargingSession);
    }

    @Override
    public ChargingSession get(String id) {
        return sessionMap.get(id);
    }

    @Override
    public List<ChargingSession> getAll() {
        return new ArrayList<>(sessionMap.values());
    }
}
