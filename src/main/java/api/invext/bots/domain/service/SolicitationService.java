package api.invext.bots.domain.service;

import api.invext.bots.domain.model.Solicitation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SolicitationService {

    private final AttendanceService attendanceService;

    public SolicitationService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    public List<Solicitation> getAll() {
        return new ArrayList<>(attendanceService.getAllSolicitation());
    }

    public Solicitation create(Solicitation solicitation) {
        return attendanceService.create(solicitation);
    }
}
