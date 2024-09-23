package api.invext.bots.domain.service;

import api.invext.bots.domain.enums.ServiceType;
import api.invext.bots.domain.exception.InvalidServiceTypeException;
import api.invext.bots.domain.model.Attendant;
import api.invext.bots.domain.model.Solicitation;
import api.invext.bots.domain.utils.DomainConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @InjectMocks
    AttendanceService attendanceService;

    @Mock
    AttendantService attendantService;

    @Test
    void test_add_to_queue() {
        Solicitation solicitation = new Solicitation();
        solicitation.setSubject("Problemas com Cartão");
        solicitation.setDescription("Preciso de ajuda com a conta");

        attendanceService.addToQueue(solicitation);

        Queue<Solicitation> queue = attendanceService.getAllSolicitation();
        assertTrue(queue.contains(solicitation));
    }

    @Test
    void test_retrieving_all_solicitations_from_queue() {
        Solicitation solicitation1 = new Solicitation();
        solicitation1.setSubject("Problemas com Cartão");
        solicitation1.setDescription("Preciso de ajuda com o cartão");

        Solicitation solicitation2 = new Solicitation();
        solicitation2.setSubject("Problemas com Cartão");
        solicitation2.setDescription("Cartão quebrou");

        attendanceService.addToQueue(solicitation1);
        attendanceService.addToQueue(solicitation2);

        Queue<Solicitation> allSolicitations = attendanceService.getAllSolicitation();

        assertTrue(allSolicitations.contains(solicitation1));
        assertTrue(allSolicitations.contains(solicitation2));
    }

    @Test
    void test_create_solicitation_and_assign_to_attendant() {
        Solicitation solicitation = new Solicitation();
        solicitation.setSubject("Outros Assuntos");
        solicitation.setDescription("Esqueci a senha");
        solicitation.setCostumerID(123L);

        Attendant attendant = new Attendant();
        attendant.setName("João");
        attendant.setServiceType(ServiceType.OTHER_SERVICES);

        when(attendantService.getAllByType(solicitation.getSubject())).thenReturn(List.of(attendant));

        Assertions.assertDoesNotThrow(() -> attendanceService.create(solicitation));
        assertTrue(attendant.getSolicitationList().contains(solicitation));
    }

    @Test
    void test_add_solicitation_to_full_attendant_list() {
        Attendant fullAttendant = new Attendant();
        fullAttendant.setName("João");
        fullAttendant.setServiceType(ServiceType.OTHER_SERVICES);
        for (int i = 0; i < DomainConstants.SOLICITATION_LIMIT_PER_ATTENDANT; i++) {
            Solicitation solicitation = new Solicitation();
            solicitation.setSubject("Outros Assuntos");
            solicitation.setDescription("Solicitacão " + i);
            fullAttendant.addSolicitation(solicitation);
        }

        Solicitation newAboveLimitSolicitation = new Solicitation();
        newAboveLimitSolicitation.setSubject("Outros Assuntos");
        newAboveLimitSolicitation.setDescription("Solicitação acima do limite");

        when(attendantService.getAllByType(newAboveLimitSolicitation.getSubject()))
                .thenReturn(List.of(fullAttendant));

        attendanceService.create(newAboveLimitSolicitation);

        assertTrue(attendanceService.getAllSolicitation().contains(newAboveLimitSolicitation));
    }

    @Test
    void test_handling_solicitations_with_invalid_subject() {
        Solicitation solicitation = new Solicitation();
        solicitation.setSubject("Invalid_Subject");
        solicitation.setDescription("Test Description");
        solicitation.setCostumerID(123L);

        when(attendantService.getAllByType(solicitation.getSubject()))
                .thenThrow(new InvalidServiceTypeException("Invalid service type, you should choose 'Problemas com Cartão'," +
                        " 'Contratação de empréstimo' or 'Outros Assuntos'."));

        Assertions.assertThrows(InvalidServiceTypeException.class, () -> attendanceService.create(solicitation));
    }

}
