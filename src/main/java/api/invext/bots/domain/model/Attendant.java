package api.invext.bots.domain.model;

import api.invext.bots.domain.enums.ServiceType;
import api.invext.bots.domain.utils.DomainConstants;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
public class Attendant {

    String name;

    List<Solicitation> solicitationList = new LinkedList<>();

    ServiceType serviceType;

    public final boolean isAvailable() {
        return this.solicitationList.size() != DomainConstants.SOLICITATION_LIMIT_PER_ATTENDANT;
    }

    public final void addSolicitation(Solicitation solicitation) {
        solicitation.setStartedResolution(LocalDateTime.now());
        this.solicitationList.add(solicitation);
    }
}
