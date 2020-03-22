package com.oguz.demo.evechargingsessions.event;

import com.oguz.demo.evechargingsessions.entity.ChargingSession;
import org.springframework.context.ApplicationEvent;

public class SessionStartedEvent extends ApplicationEvent {
    public SessionStartedEvent(ChargingSession source) {
        super(source);
    }
}
