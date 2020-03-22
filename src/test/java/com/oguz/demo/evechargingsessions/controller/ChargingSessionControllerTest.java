package com.oguz.demo.evechargingsessions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oguz.demo.evechargingsessions.dto.request.IdentityRequest;
import com.oguz.demo.evechargingsessions.dto.response.ChargingSessionDto;
import com.oguz.demo.evechargingsessions.entity.ChargingSession;
import com.oguz.demo.evechargingsessions.entity.Status;
import com.oguz.demo.evechargingsessions.service.session.ChargingSessionService;
import com.oguz.demo.evechargingsessions.service.summary.SessionSummaryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.oguz.demo.evechargingsessions.controller.IntegrationTestUtil.APPLICATION_JSON_UTF8;
import static com.oguz.demo.evechargingsessions.controller.IntegrationTestUtil.convertObjectToJsonBytes;
import static com.oguz.demo.evechargingsessions.util.Util.generateRandomUUID;
import static com.oguz.demo.evechargingsessions.util.Util.getCurrentDateTime;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ChargingSessionController.class)
public class ChargingSessionControllerTest {

    @MockBean
    SessionSummaryService summaryService;

    @MockBean
    ChargingSessionService sessionService;

    @Autowired
    private MockMvc mockMvc;

    private IdentityRequest identityRequest;
    private ChargingSessionDto startedDto;
    private ChargingSessionDto stoppedDto;
    private static final String startedUUID = generateRandomUUID();
    private static final String STATION_ID = "abc_123";

    @Before
    public void init() {
        JacksonTester.initFields(this, new ObjectMapper());

        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setStatus(Status.IN_PROGRESS);
        chargingSession.setStartedAt(getCurrentDateTime());
        chargingSession.setId(startedUUID);
        chargingSession.setStationId(STATION_ID);

        startedDto = ChargingSessionDto.of(chargingSession);


        chargingSession.setStoppedAt(getCurrentDateTime());
        chargingSession.setStatus(Status.FINISHED);
        stoppedDto = ChargingSessionDto.of(chargingSession);

        identityRequest = new IdentityRequest();
        identityRequest.setStationId(STATION_ID);
    }

    @Test
    public void testSubmitSession_Success() throws Exception {
        given(sessionService.start(any(IdentityRequest.class))).willReturn(startedDto);
        mockMvc.perform(post("/chargingSessions")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(identityRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(startedDto.getId())))
                .andExpect(jsonPath("$.stationId", is(startedDto.getStationId())))
                .andExpect(jsonPath("$.startedAt").exists())
                .andExpect(jsonPath("$.status", is(startedDto.getStatus().toString())))
                .andExpect(jsonPath("$.stoppedAt").doesNotExist());
    }

    @Test
    public void testStopSession_Success() throws Exception {

        given(sessionService.stop(anyString())).willReturn(stoppedDto);
        mockMvc.perform(put("/chargingSessions/{id}", startedUUID)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(stoppedDto.getId())))
                .andExpect(jsonPath("$.stationId", is(stoppedDto.getStationId())))
                .andExpect(jsonPath("$.startedAt").exists())
                .andExpect(jsonPath("$.status", is(stoppedDto.getStatus().toString())))
                .andExpect(jsonPath("$.stoppedAt").exists());
    }

    @Test
    public void testGetAll_Success() throws Exception {
        List<ChargingSessionDto> list = new ArrayList<>();
        list.add(startedDto);
        list.add(stoppedDto);
        given(sessionService.getAll()).willReturn(list);
        mockMvc.perform(get("/chargingSessions")
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(startedDto.getId()))
                .andExpect(jsonPath("$[1].id").value(stoppedDto.getId()))
                .andExpect(jsonPath("$[0].stationId").value(startedDto.getStationId()))
                .andExpect(jsonPath("$[1].stationId").value(stoppedDto.getStationId()))
                .andExpect(jsonPath("$[0].status").value(startedDto.getStatus().toString()))
                .andExpect(jsonPath("$[1].status").value(stoppedDto.getStatus().toString()));
    }




}
