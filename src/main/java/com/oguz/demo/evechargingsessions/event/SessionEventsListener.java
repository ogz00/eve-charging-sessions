package com.oguz.demo.evechargingsessions.event;

import com.oguz.demo.evechargingsessions.service.ringbuffer.RingBufferService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class SessionEventsListener {
    private RingBufferService ringBuffer;

    @Autowired
    public SessionEventsListener(RingBufferService ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    @EventListener
    public void handleSessionStartedEvent(SessionStartedEvent sessionStartedEvent) {
        log.info("Session Started Event Listener");
        ringBuffer.increaseSessionStartedCount();
    }

    @EventListener
    public void handleSessionStoppedEvent(SessionStoppedEvent sessionStoppedEvent) {
        log.info("Session Stopped Event Listener");
        ringBuffer.increaseSessionFinishedCount();
    }
}
