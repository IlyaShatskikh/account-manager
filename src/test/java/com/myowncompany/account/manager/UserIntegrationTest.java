package com.myowncompany.account.manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("/application.yml")
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addUser() throws Exception {
        String user = """
                {
                  "username": "user1",
                  "password": "password1",
                  "email": "user@email.com"
                }
                """;

        mockMvc.perform(post("/account-manager/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/sql/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteUser() throws Exception {
        mockMvc.perform(delete("/account-manager/user/2"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/sql/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteUserFail() throws Exception {
        mockMvc.perform(delete("/account-manager/user/3"))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

}
