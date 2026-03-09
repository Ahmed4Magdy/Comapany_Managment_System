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

public class ProjectControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    @DatabaseSetup("/datasets/projects/projects-initial.xml")
    @ExpectedDatabase(value = "/datasets/projects/projects-expected-AddProject.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testCreateProject() throws Exception {


        String NewProjectJson = """
                
                {
                  "name":"Database Schema Setup",
                  "startDate":"2026-03-09",
                  "endDate":"2026-03-15"
                
                
                }
                
                
                """;


        mockMvc.perform(post("/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(NewProjectJson))
                .andDo(print())
                .andExpect(status().isCreated());


    }


    @Test
    @DatabaseSetup("/datasets/projects/projects-initial.xml")
    void testCreateProject_ShouldReturnBadRequest() throws Exception {

        String newDepartmentJson = """
                {
                    "name": ""
                }
                """;
        mockMvc.perform(post("/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newDepartmentJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DatabaseSetup("/datasets/projects/projects-initial.xml")
    @ExpectedDatabase(value = "/datasets/projects/Projects-expected-GetProjectWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testGetProjectWithId() throws Exception {


        mockMvc.perform(get("/project/1"))

                .andDo(print())
                .andExpect(status().isOk());


    }


    @Test
    @DatabaseSetup("/datasets/projects/projects-initial.xml")
    void testGetProjectWithId_ShouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/project/11"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DatabaseSetup("/datasets/projects/projects-initial.xml")
    @ExpectedDatabase(value = "/datasets/projects/Projects-expected-GetProjectWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testGetAllProjects() throws Exception {


        mockMvc.perform(get("/project"))

                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    @DatabaseSetup("/datasets/projects/projects-initial.xml")
    @ExpectedDatabase(value = "/datasets/projects/projects-expected-UpdateProjectWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testUpdateProjectWithId() throws Exception {

        String updateProject = """
                {
                  "name":"DB design",
                  "startDate":"2026-03-20",
                  "endDate":"2026-03-28"
                
                
                }
                
                """;

        mockMvc.perform(put("/project/2")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateProject))
                .andDo(print())
                .andExpect(status().isOk());


    }


    @Test
    @DatabaseSetup("/datasets/projects/projects-initial.xml")
    void testsUpdateProjectWithId_ShouldThrowProjectNotFoundException() throws Exception {


        String updateEmployee = """
                {
                
                   "name":"DB design",
                   "startDate":"2026-03-20",
                   "endDate":"2026-03-28"
                
                
                }
                
                """;


        mockMvc.perform(put("/project/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateEmployee))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not Found Project with " + 11));

    }


    @Test
    @DatabaseSetup("/datasets/projects/projects-initial.xml")
    @ExpectedDatabase(value = "/datasets/projects/projects-expected-DeleteProject.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testDeleteProjectWithId() throws Exception {

        mockMvc.perform(delete("/project/2"))
                .andDo(print())
                .andExpect(status().isNoContent());


    }



    @Test
    @DatabaseSetup("/datasets/projects/projects-initial-ProjectHasTasks.xml")
    void testDeleteProjectWithId_ShouldReturnConflict_containONTask() throws Exception {

        mockMvc.perform(delete("/project/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Cannot delete project if tasks belong to it "));
    }



}
