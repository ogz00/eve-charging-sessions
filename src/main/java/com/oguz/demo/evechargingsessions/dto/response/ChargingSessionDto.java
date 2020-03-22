package com.oguz.demo.evechargingsessions.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oguz.demo.evechargingsessions.entity.ChargingSession;
import com.oguz.demo.evechargingsessions.entity.Status;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ChargingSessionDto is the main entity we'll be using to communicate with API
 *
 * @author Oğuzhan Karacüllü
 */
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargingSessionDto implements Serializable {
    private static final long serialVersionUID = 123L;

    /**
     * unique uuid string
     */
    private String id;
    /**
     * station id given by user
     */
    private String stationId;
    /**
     * started date-time for session
     */
    private LocalDateTime startedAt;
    /**
     * stopped date-time for session
     */
    private LocalDateTime stoppedAt;

    /**
     * current status of session
     */
    private Status status;

    public static ChargingSessionDto of(final ChargingSession chargingSession) {
        return new ChargingSessionDto(chargingSession.getId(),
                chargingSession.getStationId(),
                chargingSession.getStartedAt(),
                chargingSession.getStoppedAt(),
                chargingSession.getStatus());
    }
}
