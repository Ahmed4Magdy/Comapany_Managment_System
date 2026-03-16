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

public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    @ExpectedDatabase(value = "/datasets/tasks/tasks-expected-AddTask.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testCreateTask() throws Exception {


        String NewProjectJson = """
                
                {
                  "title":"Database Schema Setup",
                  "description":"Design Er",
                  "deadline":"2026-03-15",
                  "employeeId":1,
                  "projectId":1
                
                
                }
                
                
                """;


        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(NewProjectJson))
                .andDo(print())
                .andExpect(status().isCreated());


    }


    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial-hasNoActiveEmployee.xml")
    void testCreateTask_shouldThrowTaskNoStoreInActiveEmployeeException() throws Exception {


        String NewProjectJson = """
                
                {
                  "title":"Database Schema Setup",
                  "description":"Design Er",
                  "deadline":"2026-03-15",
                  "employeeId":1,
                  "projectId":1
                
                
                }
                
                
                """;


        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(NewProjectJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("can't create task have inactive employee "));


    }


    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    void testCreateTask_shouldThrowProjectNotFoundException() throws Exception {


        String NewProjectJson = """
                
                {
                  "title":"Database Schema Setup",
                  "description":"Design Er",
                  "deadline":"2026-03-15",
                  "employeeId":1,
                  "projectId":2
                
                
                }
                
                
                """;


        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(NewProjectJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not Found Project "+ 2));


    }


    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    void testCreateTask_ShouldReturnBadRequest() throws Exception {

        String newDepartmentJson = """
                {
                    "title": ""
                }
                """;
        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newDepartmentJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }



    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    @ExpectedDatabase(value = "/datasets/tasks/Tasks-expected-GetTaskWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testGetTaskWithId() throws Exception {


        mockMvc.perform(get("/task/1"))

                .andDo(print())
                .andExpect(status().isOk());


    }


    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    void testGetTaskWithId_ShouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/task/11"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not Found task "+ 11));
    }



    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    @ExpectedDatabase(value = "/datasets/tasks/Tasks-expected-GetAllTasks.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testGetAllTasks() throws Exception {


        mockMvc.perform(get("/task"))

                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    @ExpectedDatabase(value = "/datasets/tasks/tasks-expected-UpdateTaskWithId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testUpdateProjectWithId() throws Exception {

        String updateTask = """
                {
                    "title":"setup front",
                    "deadline":"2026-03-11",
                    "description":"Design UI"
               
             
                
                }
                
                """;

        mockMvc.perform(put("/task/2")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateTask))
                .andDo(print())
                .andExpect(status().isOk());


    }


    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    void testsUpdateTaskWithId_ShouldThrowTaskNotFoundException() throws Exception {


        String updateEmployee = """
                {
                
                    "title":"setup front",
                    "deadline":"2026-03-11",
                    "description":"Design UI"
                     
                

                }
                
                """;


        mockMvc.perform(put("/task/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateEmployee))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not Found task "+ 11));

    }


    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    void testsUpdateTaskWithId_ShouldThrowEmployeeIdNotFoundException() throws Exception {


        String updateEmployee = """
                {
                
                    "title":"setup front",
                    "deadline":"2026-03-11",
                    "description":"Design UI",
                    "employeeId":11
                
                

                }
                
                """;


        mockMvc.perform(put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateEmployee))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not Found Employee "+ 11));

    }


    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    void testsUpdateTaskWithId_ShouldThrowProjectIdNotFoundException() throws Exception {


        String updateEmployee = """
                {
                
                    "title":"setup front",
                    "deadline":"2026-03-11",
                    "description":"Design UI",
                    "projectId":11
                
                

                }
                
                """;


        mockMvc.perform(put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateEmployee))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not Found Project with "+ 11));

    }


    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    @ExpectedDatabase(value = "/datasets/tasks/tasks-expected-GetAllTasksWithProjectId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testGetAllTasksWithProjectId() throws Exception {


        mockMvc.perform(get("/task//withProject/1"))

                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    @ExpectedDatabase(value = "/datasets/tasks/tasks-expected-GetAllTasksWithProjectId.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testGetAllTasksWithEmployeeId() throws Exception {


        mockMvc.perform(get("/task//withEmployee/1"))

                .andDo(print())
                .andExpect(status().isOk());


    }




    @Test
    @DatabaseSetup("/datasets/tasks/tasks-initial.xml")
    @ExpectedDatabase(value = "/datasets/tasks/tasks-expected-DeleteTask.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    void testsDeleteEmployeeWithId() throws Exception {


        mockMvc.perform(delete("/task/2"))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

}
