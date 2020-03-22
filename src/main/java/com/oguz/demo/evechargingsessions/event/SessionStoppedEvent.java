package com.oguz.demo.evechargingsessions.event;

import com.oguz.demo.evechargingsessions.entity.ChargingSession;
import org.springframework.context.ApplicationEvent;

public class SessionStoppedEvent extends ApplicationEvent {
    public SessionStoppedEvent(ChargingSession source) {
        super(source);
    }
}
