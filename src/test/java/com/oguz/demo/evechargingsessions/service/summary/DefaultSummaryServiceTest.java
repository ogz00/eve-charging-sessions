package com.oguz.demo.evechargingsessions.service.summary;

import com.oguz.demo.evechargingsessions.dto.response.SummaryDto;
import com.oguz.demo.evechargingsessions.exception.SummaryFetchingFailedException;
import com.oguz.demo.evechargingsessions.service.ringbuffer.RingBufferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSummaryServiceTest {
    @InjectMocks
    DefaultSummaryService service;

    @Mock
    RingBufferService ringBufferService;

    @Test
    public void getSummaryTest_Success() {
        when(ringBufferService.getSessionStartedSummary()).thenReturn(2L);
        when(ringBufferService.getSessionStoppedSummary()).thenReturn(1L);

        SummaryDto dto = service.getSummary();
        assertNotNull(dto);
        assertEquals(2L, dto.getStartedCount());
        assertEquals(1L, dto.getStoppedCount());
        assertEquals(3L, dto.getTotalCount());
    }

    @Test
    public void getSummaryTest_Failed() {
        when(ringBufferService.getSessionStartedSummary()).thenThrow(new RuntimeException());
        try {
            service.getSummary();
        }catch (Exception ex) {
            assertNotNull(ex);
            assertTrue(ex instanceof SummaryFetchingFailedException);
        }

    }
}
