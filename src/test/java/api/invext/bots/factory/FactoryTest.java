package api.invext.bots.factory;

import api.invext.bots.domain.enums.ServiceType;
import api.invext.bots.domain.model.Attendant;
import api.invext.bots.domain.model.Solicitation;

import java.util.ArrayList;
import java.util.List;

public class FactoryTest {

    public Attendant getAttendant() {
        Attendant attendant = new Attendant();
        attendant.setName("Jose");
        attendant.setServiceType(ServiceType.CARDS);

        return attendant;
    }

    public Solicitation getSolicitation() {
        Solicitation solicitation = new Solicitation();
        solicitation.setSubject("Problemas com Cartão");
        solicitation.setDescription("Preciso de ajuda com a conta");
        solicitation.setCostumerID(1L);

        return solicitation;
    }

    public List<Solicitation> getSolicitationList(Integer listSize) {
        List<Solicitation> solicitationList = new ArrayList<>();

        for (int i = 0; i < listSize; i++) {
            Solicitation solicitation = new Solicitation();
            solicitation.setSubject("Problemas com Cartão");
            solicitation.setDescription("Solicitacão " + i);
            solicitation.setCostumerID(1L);

            solicitationList.add(solicitation);
        }

        return solicitationList;
    }

}
