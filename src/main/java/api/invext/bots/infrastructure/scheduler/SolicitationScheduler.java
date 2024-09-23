package api.invext.bots.infrastructure.scheduler;

import api.invext.bots.domain.service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SolicitationScheduler {

    private final AttendanceService attendanceService;

    public SolicitationScheduler(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    private static final Logger log = LoggerFactory.getLogger(SolicitationScheduler.class);


    @Scheduled(fixedDelay = 20, timeUnit = TimeUnit.SECONDS)
    public void solicitationHandlerExecutor() {
        log.info("Starting to verify pending solicitations");
        this.attendanceService.removeFinishedSolicitationsFromAttendants();
        this.attendanceService.sendNewSolicitationsToAttendants();

    }
}
