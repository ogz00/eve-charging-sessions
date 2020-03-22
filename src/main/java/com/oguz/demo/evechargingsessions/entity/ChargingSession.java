package com.oguz.demo.evechargingsessions.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChargingSession {
    private String id;
    private String stationId;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
    private Status status;
}
