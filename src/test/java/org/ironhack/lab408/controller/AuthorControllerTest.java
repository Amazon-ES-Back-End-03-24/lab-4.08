package org.ironhack.lab408.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.lab408.dtos.AuthorDTO;
import org.ironhack.lab408.model.Author;
import org.ironhack.lab408.repository.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class AuthorControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AuthorRepository authorRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Author author;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        author = new Author("John Doe");
        author = authorRepository.save(author);
    }

    @AfterEach
    void tearDown() {
        authorRepository.deleteAll();
    }

    @Test
    void findById_existingId_authorReturned() throws Exception {
        MvcResult result = mockMvc.perform(get("/authors/{id}", author.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("John Doe"));
    }

    @Test
    void findById_nonExistingId_throwsNotFound() throws Exception {
        mockMvc.perform(get("/authors/{id}", 0L))
                .andExpect(status().isNotFound());
    }

    @Test
    void store_validAuthor_authorCreated() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO("Jane Doe");
        String body = objectMapper.writeValueAsString(authorDTO);

        MvcResult result = mockMvc.perform(post("/authors")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Jane Doe"));
    }

    @Test
    void update_existingId_authorUpdated() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO("Updated Name");
        String body = objectMapper.writeValueAsString(authorDTO);

        mockMvc.perform(put("/authors/{id}", author.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Author updated = authorRepository.findById(author.getId()).get();
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void update_nonExistingId_throwsNotFound() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO("Updated Name");
        String body = objectMapper.writeValueAsString(authorDTO);

        mockMvc.perform(put("/authors/{id}", 0L)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_existingId_authorDeleted() throws Exception {
        mockMvc.perform(delete("/authors/{id}", author.getId()))
                .andExpect(status().isNoContent());

        assertFalse(authorRepository.findById(author.getId()).isPresent());
    }

    @Test
    void delete_nonExistingId_throwsNotFound() throws Exception {
        mockMvc.perform(delete("/authors/{id}", 0L))
                .andExpect(status().isNotFound());
    }
}