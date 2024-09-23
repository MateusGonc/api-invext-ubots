package api.invext.bots.domain.enums;

import api.invext.bots.domain.exception.InvalidServiceTypeException;

import java.util.Arrays;

public enum ServiceType {
    CARDS("Problemas com Cartão"),
    LOAN("Contratação de empréstimo"),
    OTHER_SERVICES("Outros Assuntos");

    private final String subject;

    ServiceType(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public static ServiceType getBySubject(String subject) {
        return Arrays.stream(ServiceType.values())
                .filter(s -> s.getSubject().equalsIgnoreCase(subject))
                .findFirst()
                .orElseThrow(() -> new InvalidServiceTypeException("Invalid service type, you should choose 'Problemas com Cartão'," +
                        " 'Contratação de empréstimo' or 'Outros Assuntos'."));
    }

}
