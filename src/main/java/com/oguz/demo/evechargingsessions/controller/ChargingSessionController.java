package com.oguz.demo.evechargingsessions.controller;

import com.oguz.demo.evechargingsessions.dto.request.IdentityRequest;
import com.oguz.demo.evechargingsessions.dto.response.ChargingSessionDto;
import com.oguz.demo.evechargingsessions.dto.response.SummaryDto;
import com.oguz.demo.evechargingsessions.service.session.ChargingSessionService;
import com.oguz.demo.evechargingsessions.service.summary.SessionSummaryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * ChargingSessionController is the main controller class we'll be using to manage application
 *
 * @author Oğuzhan Karacüllü
 */
@RestController
@RequestMapping(value = "/chargingSessions", produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2
public class ChargingSessionController {

    private ChargingSessionService chargingSessionService;
    private SessionSummaryService sessionSummaryService;

    @Autowired
    public ChargingSessionController(ChargingSessionService chargingSessionService,
                                     SessionSummaryService sessionSummaryService) {
        this.chargingSessionService = chargingSessionService;
        this.sessionSummaryService = sessionSummaryService;
    }

    /**
     * <p>Expects station id in body json </p>
     * Please see the {@link com.oguz.demo.evechargingsessions.dto.request.IdentityRequest}
     * @param request object for start new session with given station id
     * @return created ResponseEntity as a Json and 200 OK
     * Please see the {@link com.oguz.demo.evechargingsessions.dto.response.ChargingSessionDto}
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity<ChargingSessionDto> submitSession(@Valid @RequestBody IdentityRequest request) {
        log.info("submit Charging Session request");
        return new ResponseEntity<>(chargingSessionService.start(request), HttpStatus.OK);
    }

    /**
     * <p>Expects session id in PathVariable for stop the session </p>
     * @param id Unique string value for stop the session with given id
     * @return created ResponseEntity as a Json and 200 OK
     * Please see the {@link com.oguz.demo.evechargingsessions.dto.response.ChargingSessionDto}
     * @since 1.0
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChargingSessionDto> stopChargingSession(@PathVariable String id) {
        log.info("stop Charging Session request");
        return new ResponseEntity<>(chargingSessionService.stop(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ChargingSessionDto>> getAllSessions() {
        log.info("get All request");
        return new ResponseEntity<>(chargingSessionService.getAll(), HttpStatus.OK);
    }


    /**
     * <p>Generate summary report for last minute actions</p>
     *
     * @return created SummaryDto as a Json and 200 OK
     * Please see the {@link com.oguz.demo.evechargingsessions.dto.response.SummaryDto}
     * @since 1.0
     */
    @GetMapping("/summary")
    public ResponseEntity<SummaryDto> getSummary() {
        log.info("get Summary request");
        return new ResponseEntity<>(sessionSummaryService.getSummary(), HttpStatus.OK);
    }
}
