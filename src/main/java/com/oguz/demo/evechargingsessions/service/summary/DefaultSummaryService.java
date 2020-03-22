package com.oguz.demo.evechargingsessions.service.summary;

import com.oguz.demo.evechargingsessions.dto.response.SummaryDto;
import com.oguz.demo.evechargingsessions.exception.SummaryFetchingFailedException;
import com.oguz.demo.evechargingsessions.service.ringbuffer.RingBufferService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SessionSummaryService is the main service we'll be using to generate service usage reports
 *
 * Please see the {@link com.oguz.demo.evechargingsessions.service.summary.SessionSummaryService} interface for core service design
 * @author Oğuzhan Karacüllü
 *
 */
@Log4j2
@Service
public class DefaultSummaryService implements SessionSummaryService {

    private RingBufferService ringBuffer;

    @Autowired
    public DefaultSummaryService(RingBufferService ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * <p>Generates last minute actions summary</p>
     * @return created SummaryDto object, as  a summary report
     * @since 1.0
     */
    @Override
    public SummaryDto getSummary() {
        try {
            long started = ringBuffer.getSessionStartedSummary();
            long stopped = ringBuffer.getSessionStoppedSummary();
            return SummaryDto.builder()
                    .startedCount(started)
                    .stoppedCount(stopped)
                    .totalCount(started + stopped)
                    .build();
        } catch (RuntimeException ex) {
            throw new SummaryFetchingFailedException("An internal error occurred when generate sess,on summary report");
        }
    }
}
