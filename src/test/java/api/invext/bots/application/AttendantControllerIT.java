package api.invext.bots.application;

import api.invext.bots.domain.enums.ServiceType;
import api.invext.bots.domain.model.Attendant;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class AttendantControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    void getAttendantNotFoundExceptionTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/attendant/Joao"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        Assertions.assertEquals("Not Found user with name: Joao", result.getResponse().getContentAsString());
    }

    @Test
    void insertAttendantAndGetInsertedAttendantTest() throws Exception {
        Attendant attendant = new Attendant();
        attendant.setName("Jose");
        attendant.setServiceType(ServiceType.OTHER_SERVICES);

        mockMvc.perform(MockMvcRequestBuilders.post("/attendant")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(attendant)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/attendant/Jose"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Attendant resultAttendant = mapper.readValue(result.getResponse().getContentAsString(), Attendant.class);

        Assertions.assertEquals(attendant.getName(), resultAttendant.getName());
        Assertions.assertEquals(attendant.getServiceType(), resultAttendant.getServiceType());
    }
}
