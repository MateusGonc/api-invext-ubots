package api.invext.bots.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Solicitation {

    String subject;

    String description;

    Long costumerID;

    LocalDateTime startedResolution;
}
