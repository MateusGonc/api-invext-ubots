package api.invext.bots.domain.service;

import api.invext.bots.domain.enums.ServiceType;
import api.invext.bots.domain.exception.RegisterNotFoundException;
import api.invext.bots.domain.model.Attendant;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AttendantService {

    Map<ServiceType, List<Attendant>> mapAttendantType = new EnumMap<>(ServiceType.class);

    public AttendantService() {
        for (ServiceType type : ServiceType.values()) {
            mapAttendantType.put(type, new LinkedList<>());
        }
    }

    public List<Attendant> getAll() {
        return mapAttendantType.values().stream()
                .flatMap(Collection::stream)
                .toList();
    }

    public List<Attendant> getAllByType(String typeSubject) {
        ServiceType type = ServiceType.getBySubject(typeSubject);
        return this.mapAttendantType.get(type);
    }

    public Attendant getByName(String name) {
        return mapAttendantType.values().stream().flatMap(Collection::stream)
                .filter(attendant -> attendant.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RegisterNotFoundException("Not Found user with name: " + name));
    }

    public Attendant create(Attendant attendant) {
        this.mapAttendantType.get(attendant.getServiceType())
                .add(attendant);

        return attendant;
    }

}
