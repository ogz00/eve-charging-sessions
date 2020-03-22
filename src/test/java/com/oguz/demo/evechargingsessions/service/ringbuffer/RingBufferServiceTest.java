package com.oguz.demo.evechargingsessions.service.ringbuffer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RingBufferServiceTest {

    RingBufferService ringBufferService;

    private static final Duration ONE_MIN = Duration.ofMinutes(1L);

//    @BeforeEach
//    public void init() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime oneMinBefore = LocalDateTime.now().minus(ONE_MIN);
//
//    }

    @BeforeEach
    public void resetSingleton() throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        Constructor<?> privateConstructor = RingBufferService.class.getDeclaredConstructors()[0];
        privateConstructor.setAccessible(true);

        Field instance = RingBufferService.class.getDeclaredField("instance");
        instance.setAccessible(true);

        instance.set(null, privateConstructor.newInstance());
    }

    @Test
    public void TotalStartedSessionCountInLastMinuteTest() {
        ringBufferService = RingBufferService.instance();
        ringBufferService.increaseSessionStartedCount();
        ringBufferService.increaseSessionStartedCount();
        assertEquals(2L, ringBufferService.getSessionStartedSummary());
        assertEquals(0L, ringBufferService.getSessionStoppedSummary());
    }

    @Test
    public void TotalFinishedSessionCountInLastMinuteTest() {
        ringBufferService = RingBufferService.instance();
        ringBufferService.increaseSessionFinishedCount();
        ringBufferService.increaseSessionFinishedCount();
        assertEquals(2L, ringBufferService.getSessionStoppedSummary());
        assertEquals(0L, ringBufferService.getSessionStartedSummary());
    }

}
