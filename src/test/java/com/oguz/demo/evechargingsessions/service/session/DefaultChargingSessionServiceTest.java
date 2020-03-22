package com.oguz.demo.evechargingsessions.service.session;

import com.oguz.demo.evechargingsessions.dto.request.IdentityRequest;
import com.oguz.demo.evechargingsessions.dto.response.ChargingSessionDto;
import com.oguz.demo.evechargingsessions.entity.ChargingSession;
import com.oguz.demo.evechargingsessions.entity.Status;
import com.oguz.demo.evechargingsessions.exception.SessionNotFoundException;
import com.oguz.demo.evechargingsessions.repository.ChargingSessionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;

import static com.oguz.demo.evechargingsessions.exception.ExceptionConstants.SESSION_NOT_FOUND;
import static com.oguz.demo.evechargingsessions.util.Util.getCurrentDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultChargingSessionServiceTest {

    @InjectMocks
    DefaultChargingSessionService service;

    @Mock
    private ChargingSessionRepository chargingSessionRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    private IdentityRequest idReq;
    private ChargingSession mockStopSession;
    private ChargingSession mockStartedSession;
    private static final String STATION_ID = "abc_123";
    private static final String TEST_UUID = "random-uuid-123";

    @Before
    public void init() {
        idReq = mock(IdentityRequest.class);
        mockStopSession = mock(ChargingSession.class);
        when(mockStopSession.getId()).thenReturn(TEST_UUID);
        when(mockStopSession.getStationId()).thenReturn(STATION_ID);
        when(mockStopSession.getStartedAt()).thenReturn(getCurrentDateTime());
        when(mockStopSession.getStoppedAt()).thenReturn(getCurrentDateTime());
        when(mockStopSession.getStatus()).thenReturn(Status.FINISHED);

        when(idReq.getStationId()).thenReturn(STATION_ID);
        doNothing().when(chargingSessionRepository).put(any(ChargingSession.class));
        when(chargingSessionRepository.get(anyString())).thenReturn(mockStopSession);

    }

    @Test
    public void startTest_Success() {
        ChargingSessionDto dto = service.start(idReq);
        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertEquals(dto.getStationId(), STATION_ID);
        assertEquals(dto.getStatus(), Status.IN_PROGRESS);
        assertNotNull(dto.getStartedAt());
    }

    @Test
    public void stopTest_Success() {
        ChargingSessionDto dto = service.stop(TEST_UUID);
        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertEquals(dto.getId(), TEST_UUID);
        assertEquals(dto.getStationId(), STATION_ID);
        assertEquals(dto.getStatus(), Status.FINISHED);
        assertNotNull(dto.getStoppedAt());
        assertTrue(dto.getStoppedAt().isBefore(getCurrentDateTime()));
    }

    @Test
    public void stopTest_Failed() {
        when(chargingSessionRepository.get(anyString())).thenReturn(null);
       try{
           service.stop(TEST_UUID);
       }catch (SessionNotFoundException ex) {
           assertNotNull(ex);
           assertEquals(ex.getMessage(), String.format(SESSION_NOT_FOUND, TEST_UUID));
       }
    }

    @Test
    public void getAll_Success() {
        mockStartedSession = mock(ChargingSession.class);
        when(mockStartedSession.getId()).thenReturn(TEST_UUID);
        when(mockStartedSession.getStationId()).thenReturn(STATION_ID);
        when(mockStartedSession.getStartedAt()).thenReturn(getCurrentDateTime());
        when(mockStartedSession.getStatus()).thenReturn(Status.FINISHED);
        List<ChargingSession> list = new ArrayList<>();
        list.add(mockStartedSession);
        list.add(mockStopSession);
        when(chargingSessionRepository.getAll()).thenReturn(list);
        List<ChargingSessionDto> dtoList = service.getAll();
        assertNotNull(dtoList);
        assertEquals(dtoList.size(), 2);
    }

}
