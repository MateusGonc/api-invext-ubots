package api.invext.bots.domain.utils;

public class DomainConstants {

    private DomainConstants() throws IllegalStateException {
        throw new IllegalStateException("Constants class must not be instantiated");
    }

    public static final Integer SOLICITATION_LIMIT_PER_ATTENDANT = 3;
    public static final Integer TIME_TO_SOLVE_SOLICITATION = 1;
}
