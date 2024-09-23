package api.invext.bots.domain.exception;

public class InvalidServiceTypeException extends RuntimeException {
    public InvalidServiceTypeException(String message) {
        super(message);
    }
}