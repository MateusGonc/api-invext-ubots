package api.invext.bots.domain.service;

import api.invext.bots.domain.model.Attendant;
import api.invext.bots.domain.model.Solicitation;
import api.invext.bots.domain.utils.DomainConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendantService attendantService;

    public AttendanceService(AttendantService attendantService) {
        this.attendantService = attendantService;
    }

    private static final Logger log = LoggerFactory.getLogger(AttendanceService.class);

    private final Queue<Solicitation> solicitationQueue = new ConcurrentLinkedQueue<>();

    public void addToQueue(Solicitation solicitation) {
        solicitationQueue.add(solicitation);
    }

    public Queue<Solicitation> getAllSolicitation() {
        return this.solicitationQueue;
    }

    public Solicitation create(Solicitation solicitation) {
        List<Attendant> attendantList = attendantService.getAllByType(solicitation.getSubject());
        boolean isAcceptedSolicitation = false;

        for (Attendant attendant : attendantList) {
            if (attendant.isAvailable()) {
                attendant.addSolicitation(solicitation);
                isAcceptedSolicitation = true;
                break;
            }
        }

        if (!isAcceptedSolicitation) {
            addToQueue(solicitation);
        }

        return solicitation;
    }

    public void removeFinishedSolicitationsFromAttendants() {
        this.attendantService.getAll().forEach(attendant -> attendant.setSolicitationList(
                attendant.getSolicitationList().stream()
                        .filter(solicitation -> {
                            boolean isSolicitationStillValid = solicitation.getStartedResolution()
                                    .isAfter((LocalDateTime.now().minusMinutes(DomainConstants.TIME_TO_SOLVE_SOLICITATION)));

                            if (!isSolicitationStillValid) {
                                log.info(String.format("Solicitation time finished, removing solicitation description: %s", solicitation.getDescription()));
                            }

                            return isSolicitationStillValid;
                        })
                        .collect(Collectors.toList())));
    }

    public void sendNewSolicitationsToAttendants() {
        this.solicitationQueue.forEach(solicitation -> {
            List<Attendant> attendantList = attendantService.getAllByType(solicitation.getSubject());

            attendantList.stream().filter(Attendant::isAvailable)
                    .findFirst().ifPresent(attendant -> {
                        log.info(String.format("Adding Solicitation: %s, to attendant: %s, team: %s", solicitation.getDescription(),
                                attendant.getName(), attendant.getServiceType()));
                        attendant.addSolicitation(solicitation);
                        solicitationQueue.remove(solicitation);
                    });
        });
    }
}
