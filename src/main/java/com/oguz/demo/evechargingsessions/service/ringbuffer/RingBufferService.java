package com.oguz.demo.evechargingsessions.service.ringbuffer;

import com.oguz.demo.evechargingsessions.entity.SessionCounterHolder;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A ring buffer is an array which is used as a queue.
 * The ring buffer has a read position and a write position which marks the next position to read from and write to the ring buffer.
 * When the write position reaches the end of the array, the write position is set back to 0. The same is true for the read position.
 * http://tutorials.jenkov.com/java-performance/ring-buffer.html
 *
 * Because of the fixed one minute summary is the main requirement, I prefer Ring buffer data-struct here.
 *
 *  * @author Oğuzhan Karacüllü
 */

@Log4j2
public class RingBufferService {

    //60 second in one minute
    private static final int CAPACITY = 60;
    //Runnable task cycle
    private static final int SECOND_IN_MILLIS = 1000;
    //Session Action holder array
    private final SessionCounterHolder[] sessionCounterHolders;
    private int writePos = 0;

    //instance
    private static RingBufferService instance = new RingBufferService();

    /**
     * private constructor for singleton class definition
     */
    private RingBufferService() {
        sessionCounterHolders = new SessionCounterHolder[CAPACITY];
        log.info("Initialize RingBufferFillCount instance");

        for (int i = 0; i < CAPACITY; i++) {
            sessionCounterHolders[i] = new SessionCounterHolder();
        }

        //Scheduled task, spins the ring at every second.
        TimerTask ringSpinner = new TimerTask() {
            @Override
            public void run() {
                spinRing();
            }
        };

        Timer timer = new Timer();
        timer.schedule(ringSpinner, 0, SECOND_IN_MILLIS);
    }

    /**
     * Method for reach-out singleton class instance
     * @return RingBufferService.class instance
     */
    public static RingBufferService instance() {
        return instance;
    }

    /**
     * create new counter object for new second
     */
    private void spinRing() {
        this.put();
    }

    /**
     * increase sessionStartedCount for current sessionCounterHolder
     */
    public void increaseSessionStartedCount() {
        sessionCounterHolders[writePos].increaseStarted();
    }

    /**
     * increase sessionFinishedCount for current sessionCounterHolder
     */
    public void increaseSessionFinishedCount() {
        sessionCounterHolders[writePos].increaseFinished();
    }

    /**
     * Calculate started session action summary with stream api.
     * @return total count of started actions in last minute
     */
    public long getSessionStartedSummary() {
        return Arrays.stream(sessionCounterHolders).mapToLong(SessionCounterHolder::getStartedSessionCount).sum();
    }

    /**
     * Calculate stopped session action summary with stream api.
     * @return total count of stopped actions in last minute
     */
    public long getSessionStoppedSummary() {
        return Arrays.stream(sessionCounterHolders).mapToLong(SessionCounterHolder::getFinishedSessionCount).sum();
    }

    /**
     * Spin the ring buffer for one element
     */
    private void put() {
        this.writePos++;
        if (this.writePos >= CAPACITY) {
            this.writePos = 0;
        }
        sessionCounterHolders[this.writePos] = new SessionCounterHolder();
    }
}
