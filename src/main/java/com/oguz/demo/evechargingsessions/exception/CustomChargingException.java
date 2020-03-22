package com.oguz.demo.evechargingsessions.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomChargingException {
    private LocalDateTime dateTime;
    private String message;
    private String detail;

    public CustomChargingException(LocalDateTime dateTime, String message, String detail) {
        super();
        this.dateTime = dateTime;
        this.message = message;
        this.detail = detail;
    }

}
