package com.oguz.demo.evechargingsessions.entity;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public class SessionCounterHolder {

    private final AtomicLong startedSessionCount = new AtomicLong(0);
    private final AtomicLong finishedSessionCount = new AtomicLong(0);

    public void increaseStarted() {
        startedSessionCount.incrementAndGet();
    }

    public void increaseFinished() {
        finishedSessionCount.incrementAndGet();
    }

    public long getStartedSessionCount() {
        return startedSessionCount.get();
    }

    public long getFinishedSessionCount() {
        return finishedSessionCount.get();
    }

}
