package api.invext.bots.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.random.RandomGenerator;

@Getter
@Setter
public class Solicitation {

    Long identifier = Math.abs(RandomGenerator.getDefault().nextLong());

    String subject;

    String description;

    Long costumerID;

    LocalDateTime startedResolution;
    LocalDateTime solvedDate;
}
