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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Test
    @DatabaseSetup("/datasets/employees/employees-initial.xml")
    @ExpectedDatabase(value = "/datasets/employees/employees-AddEmployee-Expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testCreateEmployee() throws Exception {

        String newEmployeeJson = """
                {
                     "fullName": "NourAhmed",
                     "departmentId":1,
                     "email":"Lamis@gmail.com",
                     "password":"Lamis@22",
                     "position":"java developer",
                     "role":"ROLE_EMPLOYEE"
                
                
                }
                """;
        mockMvc.perform(post("/employee/addEmployee")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEmployeeJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DatabaseSetup("/datasets/employees/employees-initial.xml")
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testCreateEmployee_givenEmailAlreadyExists_ShouldReturnIsBadRequest() throws Exception {

        String newEmployeeJson = """
                {
                     "fullName": "NourAhmed",
                     "departmentId":1,
                     "email":"ahmed@gmail.com",
                     "password":"Lamis@22",
                     "position":"java developer",
                     "role":"ROLE_EMPLOYEE"
                
                
                }
                """;
        mockMvc.perform(post("/employee/addEmployee")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEmployeeJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    @DatabaseSetup("/datasets/employees/employees-initial.xml")
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testCreateEmployee_ShouldReturnBadRequest() throws Exception {

        String newDepartmentJson = """
                {
                    "fullName": ""
                }
                """;
        mockMvc.perform(post("/employee/addEmployee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newDepartmentJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DatabaseSetup("/datasets/employees/employees-initial.xml")
    @ExpectedDatabase(value = "/datasets/employees/employees-expected-GetEmployeeWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testGetEmployeeWithId() throws Exception {


        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk());

    }

    @Test
    @DatabaseSetup("/datasets/employees/employees-initial.xml")
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testGetEmployeeWithId_ShouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/employee/11"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    @DatabaseSetup("/datasets/employees/employees-initial.xml")
    @ExpectedDatabase(value = "/datasets/employees/employees-expected-GetAllEmployees.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testGetAllEmployees() throws Exception {


        mockMvc.perform(get("/employee/allEmployees"))
                .andExpect(status().isOk());

    }


    @Test
    @DatabaseSetup("/datasets/employees/employees-initial.xml")
    @ExpectedDatabase(value = "/datasets/employees/employees-expected-GetAllActiveTrueEmployees.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testsGetAllActiveTrueEmployees() throws Exception {


        mockMvc.perform(get("/employee/ActiveTrue"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DatabaseSetup("/datasets/employees/employees-initial.xml")
    @ExpectedDatabase(value = "/datasets/employees/employees-expected-GetAllActiveFalseEmployees.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testsGetAllActiveFalseEmployees() throws Exception {


        mockMvc.perform(get("/employee/ActiveFalse"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DatabaseSetup("/datasets/employees/employees-intital-OnlyActiveEmployee.xml")
    @ExpectedDatabase(value = "/datasets/employees/employees-expected-OnlyActiveEmployee.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testsGetAllActiveFalseEmployees_ShouldReturnListIsEmptyInResult() throws Exception {


        mockMvc.perform(get("/employee/ActiveFalse"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }


    @Test
    @DatabaseSetup("/datasets/employees/employees-intital-OnlyNoActiveEmployee.xml")
    @ExpectedDatabase(value = "/datasets/employees/employees-expected-OnlyNoActiveEmployee.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testsGetAllActiveTrueEmployees_ShouldReturnListIsEmptyInResult() throws Exception {


        mockMvc.perform(get("/employee/ActiveTrue"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }


    @Test
    @DatabaseSetup("/datasets/employees/employees-initial.xml")
    @ExpectedDatabase(value = "/datasets/employees/employees-expected-UpdateEmployeeWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testsUpdateEmployeeWithId() throws Exception {


        String updateEmployee = """
                {
                
                                  "fullName":"Hassan nour",
                                  "email":"Hassan@gmail.com",
                                  "password":"shams@11",
                                  "active":"True",
                                  "departmentId":1,
                                  "position":"java developer",
                                  "role":"ROLE_EMPLOYEE",
                                  "hireDate":"2026-12-12"
                
                
                
                }
                
                """;


        mockMvc.perform(put("/employee/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateEmployee))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @DatabaseSetup("/datasets/employees/employees-initial.xml")
    @ExpectedDatabase(value = "/datasets/employees/employees-expected-NoUpdateEmployeeWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testsUpdateEmployeeWithId_ShouldThrowEmployeeNotFoundException() throws Exception {


        String updateEmployee = """
                {
                
                                  "fullName":"Hassan nour",
                                  "email":"Hassan@gmail.com",
                                  "password":"shams@11",
                                  "active":"True",
                                  "departmentId":1,
                                  "position":"java developer",
                                  "role":"ROLE_EMPLOYEE",
                                  "hireDate":"2026-03-08"
                
                
                
                }
                
                """;


        mockMvc.perform(put("/employee/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateEmployee))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not Found Employee " + 11));

    }

    @Test
    @DatabaseSetup("/datasets/employees/employees-initial-NoEmployeeHaveTask.xml")
    @ExpectedDatabase(value = "/datasets/employees/employees-expected-deleteEmployee.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testsDeleteEmployeeWithId() throws Exception {

        mockMvc.perform(delete("/employee/2"))
                .andDo(print())
                .andExpect(status().isNoContent());

    }


    @Test
    @DatabaseSetup("/datasets/employees/employees-initial.xml")
    @ExpectedDatabase(value = "/datasets/employees/employees-expected-NoDeleteEmployeeWithId-NotFoundEmployeeId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//    @Transactional
    void testDeleteDepartmentWithId_ShouldReturnNotFound_NoExistDepartmentWithId() throws Exception {

        mockMvc.perform(delete("/employee/11"))
                .andDo(print())
                .andExpect(status().isNotFound());


    }


    @Test
    @DatabaseSetup("/datasets/employees/employees-initial-IfEmployeeHaveTasks.xml")
    @ExpectedDatabase(value = "/datasets/employees/employees-DeleteEmployeeIsCorrect-Expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
//    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
    void testDeleteDepartmentWithId_ShouldReturnConflict_containONEmployee() throws Exception {

        mockMvc.perform(delete("/employee/1"))
                .andExpect(status().isNoContent());
    }


}
