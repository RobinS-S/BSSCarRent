package nl.bss.carrentapi.api.calls;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.bss.carrentapi.api.controllers.UserController;
import nl.bss.carrentapi.api.dto.user.UserRegisterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiRequests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void controllerConstructs() throws Exception {
        assertThat(userController).isNotNull();
    }

    @Test
    public void registerUser() throws Exception {
        String firstName = "Julian";
        String lastName = "Bos";
        UserRegisterDto user = new UserRegisterDto("test@test.nl", "password", firstName, null, lastName, "+31", "612345678", LocalDate.of(1980, 1, 1));

        mvc.perform( post("/api/users")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("firstName").value(firstName))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName").value(lastName))
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists()); // check if email is not in and "name" equals firstname+infix+lastname
    }

    @Test
    public void getUsersApi() throws Exception {
        mvc.perform( MockMvcRequestBuilders
                .get("/api/users")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    public String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}