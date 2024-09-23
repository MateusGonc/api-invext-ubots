package api.invext.bots.domain.service;

import api.invext.bots.domain.exception.InvalidServiceTypeException;
import api.invext.bots.domain.model.Attendant;
import api.invext.bots.domain.model.Solicitation;
import api.invext.bots.domain.utils.DomainConstants;
import api.invext.bots.factory.FactoryTest;
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

    @InjectMocks
    FactoryTest factoryTest;

    @Mock
    AttendantService attendantService;

    @Test
    void addToQueueTest() {
        Solicitation solicitation = factoryTest.getSolicitation();

        attendanceService.addToQueue(solicitation);

        Queue<Solicitation> queue = attendanceService.getAllSolicitation();
        assertTrue(queue.contains(solicitation));
    }

    @Test
    void retrievingAllSolicitationsFromQueueTest() {
        List<Solicitation> solicitationList = factoryTest.getSolicitationList(2);

        solicitationList.forEach(solicitation -> attendanceService.addToQueue(solicitation));
        Queue<Solicitation> allSolicitations = attendanceService.getAllSolicitation();

        assertTrue(allSolicitations.contains(solicitationList.getFirst()));
        assertTrue(allSolicitations.contains(solicitationList.getLast()));
    }

    @Test
    void createSolicitationAndAssignToAttendantTest() {
        Attendant attendant = factoryTest.getAttendant();
        Solicitation solicitation = factoryTest.getSolicitation();

        when(attendantService.getAllByType(solicitation.getSubject())).thenReturn(List.of(attendant));

        Assertions.assertDoesNotThrow(() -> attendanceService.create(solicitation));
        assertTrue(attendant.getSolicitationList().contains(solicitation));
    }

    @Test
    void addSolicitationToFullAttendantListTest() {
        Attendant busyAttendant = factoryTest.getAttendant();
        busyAttendant.setSolicitationList(factoryTest.getSolicitationList(DomainConstants.SOLICITATION_LIMIT_PER_ATTENDANT));

        Solicitation newAboveLimitSolicitation = factoryTest.getSolicitation();

        when(attendantService.getAllByType(newAboveLimitSolicitation.getSubject()))
                .thenReturn(List.of(busyAttendant));

        attendanceService.create(newAboveLimitSolicitation);

        assertTrue(attendanceService.getAllSolicitation().contains(newAboveLimitSolicitation));
    }

    @Test
    void handlingSolicitationsWithInvalidSubjectTest() {
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
