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


public class DepartmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DatabaseSetup("/datasets/departments/departments-initial-.xml")
    @ExpectedDatabase(value = "/datasets/departments/departments-expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testCreateDepartment() throws Exception {

        String newDepartmentJson = """
                {
                    "name": "Finance"
                }
                """;
        mockMvc.perform(post("/department/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newDepartmentJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DatabaseSetup("/datasets/departments/departments-initial-.xml")
    @ExpectedDatabase(value = "/datasets/departments/departments-expected-ValidationInvalidName.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testCreateDepartment_ShouldReturnBadRequest() throws Exception {

        String newDepartmentJson = """
                {
                    "name": ""
                }
                """;
        mockMvc.perform(post("/department/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newDepartmentJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DatabaseSetup("/datasets/departments/departments-initial-.xml")
    @ExpectedDatabase(value = "/datasets/departments/departments-expected-GetWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testGetDepartmentWithId() throws Exception {

        mockMvc.perform(get("/department/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DatabaseSetup("/datasets/departments/departments-initial-.xml")
    @ExpectedDatabase(value = "/datasets/departments/departments-expected-GetWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testGetDepartmentWithId_ShouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/department/11"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    @DatabaseSetup("/datasets/departments/departments-initial-.xml")
    @ExpectedDatabase(value = "/datasets/departments/departments-expected-GetAllDepartments.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testGetAllDepartments() throws Exception {

        mockMvc.perform(get("/department"))
                .andExpect(status().isOk());
    }


    @Test
    @DatabaseSetup("/datasets/departments/departments-initial-.xml")
    @ExpectedDatabase(value = "/datasets/departments/departments-expected-updateDeptWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testUpdateDepartmentWithId() throws Exception {

        String newDepartmentJson = """
                {
                    "name": "Accountant"
                }
                """;
        mockMvc.perform(put("/department/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newDepartmentJson))
                .andExpect(status().isOk());
    }

    @Test
    @DatabaseSetup("/datasets/departments/departments-initial-.xml")
    @ExpectedDatabase(value = "/datasets/departments/departments-expected-NoupdateDeptWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testUpdateDepartmentWithId_ShouldReturnNotFound() throws Exception {

        String newDepartmentJson = """
                {
                    "name": "Accountant"
                }
                """;
        mockMvc.perform(put("/department/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newDepartmentJson))
                .andExpect(status().isNotFound());
    }


    @Test
    @DatabaseSetup("/datasets/departments/departments-initial-.xml")
    @ExpectedDatabase(value = "/datasets/departments/departments-expected-DeleteDepartmentWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testDeleteDepartmentWithId() throws Exception {


        mockMvc.perform(delete("/department/1"))
                .andExpect(status().isNoContent());
    }


    @Test
    @DatabaseSetup("/datasets/departments/departments-initial-IfHaveEmployee.xml")
    @ExpectedDatabase(value = "/datasets/departments/departments-expected-NoDeleteDepartmentWithIdAsLongAsContainfForEmployee.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testDeleteDepartmentWithId_ShouldReturnConflict_containONEmployee() throws Exception {

        mockMvc.perform(delete("/department/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Cannot delete department if employees belong to it"));
    }


    @Test
    @DatabaseSetup("/datasets/departments/departments-initial-.xml")
    @ExpectedDatabase(value = "/datasets/departments/departments-expected-NoDeleteDepartmentWithIdAsLongAsContainfForEmployee.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testDeleteDepartmentWithId_ShouldReturnNotFound_NoExistDepartmentWithId() throws Exception {

        mockMvc.perform(delete("/department/11"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}
