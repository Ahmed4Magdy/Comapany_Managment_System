package com.example.demo.IntegrationTest;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})

public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Test
    @DatabaseSetup("/datasets/employees-initial.xml")
    @ExpectedDatabase(value = "/datasets/datasets/employees-expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testCreateEmployee() throws Exception {

        String newEmployeeJson = """
            {
                 "fullName": "NourAhmed",
                 "departmentId":1,
                 "email":"Lamis@gmail.com",
                 "password":"shams@22"
                
               
            }
            """;
        mockMvc.perform(post("/employee/addEmployee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEmployeeJson))
                .andExpect(status().isCreated());
    }
}
