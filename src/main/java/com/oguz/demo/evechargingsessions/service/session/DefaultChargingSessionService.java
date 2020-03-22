package com.oguz.demo.evechargingsessions.service.session;

import com.oguz.demo.evechargingsessions.dto.request.IdentityRequest;
import com.oguz.demo.evechargingsessions.dto.response.ChargingSessionDto;
import com.oguz.demo.evechargingsessions.entity.ChargingSession;
import com.oguz.demo.evechargingsessions.entity.Status;
import com.oguz.demo.evechargingsessions.event.SessionStartedEvent;
import com.oguz.demo.evechargingsessions.event.SessionStoppedEvent;
import com.oguz.demo.evechargingsessions.exception.SessionNotFoundException;
import com.oguz.demo.evechargingsessions.repository.ChargingSessionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.oguz.demo.evechargingsessions.entity.Status.FINISHED;
import static com.oguz.demo.evechargingsessions.exception.ExceptionConstants.SESSION_NOT_FOUND;
import static com.oguz.demo.evechargingsessions.util.Util.generateRandomUUID;
import static com.oguz.demo.evechargingsessions.util.Util.getCurrentDateTime;


/**
 * ChargingSessionService is the main service we'll be using to manage charging sessions
 *
 * Please see the {@link com.oguz.demo.evechargingsessions.service.session.ChargingSessionService} interface for core service design
 * @author Oğuzhan Karacüllü
 *
 */
@Service
@Log4j2
public class DefaultChargingSessionService implements ChargingSessionService {
    private ChargingSessionRepository chargingSessionRepository;
    private ApplicationEventPublisher publisher;

    @Autowired
    public DefaultChargingSessionService(ChargingSessionRepository chargingSessionRepository,
                                         ApplicationEventPublisher publisher) {
        this.chargingSessionRepository = chargingSessionRepository;
        this.publisher = publisher;

    }

    /**
     * <p>Creates new ChangingSessions object wih generated id
     * </p>
     * @param req the station id of new sessions
     * @return created ChargingSessionDto object
     * @since 1.0
     */
    @Override
    public ChargingSessionDto start(IdentityRequest req) {
        ChargingSession session = new ChargingSession();
        session.setStationId(req.getStationId());
        session.setId(generateRandomUUID());
        session.setStartedAt(getCurrentDateTime());
        session.setStatus(Status.IN_PROGRESS);
        chargingSessionRepository.put(session);
        log.info(String.format("Session %s created by service", session.getId()));
        publisher.publishEvent(new SessionStartedEvent(session));
        return ChargingSessionDto.of(session);

    }


    /**
     * <p>Stops ChargingSession object wih id
     * </p>
     * @param id the id of stopped session
     * @return Stopped ChargingSessionDto object
     * @exception SessionNotFoundException if session doesn't exist.
     * @since 1.0
     */
    @Override
    public ChargingSessionDto stop(String id) throws SessionNotFoundException {
        Optional<ChargingSession> optSession = Optional.ofNullable(chargingSessionRepository.get(id));
        if (!optSession.isPresent()) {
            throw new SessionNotFoundException(String.format(SESSION_NOT_FOUND, id));
        }
        ChargingSession session = optSession.get();
        session.setStoppedAt(getCurrentDateTime());
        session.setStatus(FINISHED);
        chargingSessionRepository.put(session);
        log.info(String.format("Session %s stopped by service", session.getId()));
        publisher.publishEvent(new SessionStoppedEvent(session));
        return ChargingSessionDto.of(session);
    }


    /**
     * <p> This method returns all created ChargingSessionDto</p>
     * Map in-memory HashMap values to List
     *
     * @return List of ChargingSessionDto objects.
     * @since 1.0
     */
    @Override
    public List<ChargingSessionDto> getAll() {
        return chargingSessionRepository.getAll().stream().map(ChargingSessionDto::of).collect(Collectors.toList());
    }
}
