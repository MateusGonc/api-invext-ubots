package api.invext.bots.domain.service;

import api.invext.bots.domain.model.Attendant;
import api.invext.bots.domain.model.Solicitation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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

    private final List<Solicitation> solvedSolicitationList = new LinkedList<>();

    private Solicitation solicitationSolved = new Solicitation();

    public void addToQueue(Solicitation solicitation) {
        solicitationQueue.add(solicitation);
    }

    public Queue<Solicitation> getAllSolicitation() {
        return this.solicitationQueue;
    }

    public List<Solicitation> getAllSolicitationSolved() {
        return this.solvedSolicitationList;
    }

    public Solicitation create(Solicitation solicitation) {
        List<Attendant> attendantList = attendantService.getAllByType(solicitation.getSubject());
        attendantList = attendantList.stream().filter(Attendant::isAvailable).collect(Collectors.toList());

        Optional<Attendant> opAttendantAvailable = attendantList.stream()
                .min(Comparator.comparingInt(attendant -> attendant.getSolicitationList().size()));

        if (opAttendantAvailable.isPresent()) {
            Attendant attendant = opAttendantAvailable.get();
            attendant.addSolicitation(solicitation);
        } else {
            addToQueue(solicitation);
        }

        return solicitation;
    }

    public Solicitation solveSolicitationFromAttendant (String attendantName, Long solicitationId) {
        Attendant attendant = this.attendantService.getByName(attendantName);

        attendant.getSolicitationList().removeIf(solicitation -> {
            boolean foundSolicitation = solicitation.getIdentifier().equals(solicitationId);

            if(foundSolicitation) {
                solicitation.setSolvedDate(LocalDateTime.now());
                solvedSolicitationList.add(solicitation);
                solicitationSolved = solicitation;
            }

            return foundSolicitation;
        });

        CompletableFuture.runAsync(() -> sendNewSolicitationsToAttendant(attendantName));

        return solicitationSolved;
    }

    public void sendNewSolicitationsToAttendant(String attendantName) {
        Attendant attendant = this.attendantService.getByName(attendantName);
        Iterator<Solicitation> iterator = solicitationQueue.iterator();

        while (iterator.hasNext()) {
            Solicitation solicitation = iterator.next();

            if (attendant.getServiceType().getSubject().equalsIgnoreCase(solicitation.getSubject())) {
                log.info("Adding Solicitation: {}, to attendant: {}, team: {}",
                        solicitation.getDescription(), attendant.getName(), attendant.getServiceType());
                attendant.getSolicitationList().add(solicitation);
                iterator.remove();
                break;
            }
        }
    }
}
