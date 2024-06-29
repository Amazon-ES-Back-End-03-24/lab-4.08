package org.ironhack.lab408.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.lab408.dtos.AuthorDTO;
import org.ironhack.lab408.model.Author;
import org.ironhack.lab408.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthControllerUnitTest {

    // Creates an instance of the class and injects the mocks into it.
    @InjectMocks
    private AuthorController authorController;

    // Creates an instance of the mock
    @Mock
    private AuthorService authorService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

        // Initializes mocks annotated with @Mock and injects these mocks into the fields annotated with @InjectMocks.
        MockitoAnnotations.openMocks(this);

        // Sets up the MockMvc object to simulate HTTP requests and responses for the specified controller in isolation
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
    }

    @Test
    void findById_existingId_authorReturned() throws Exception {
        Author author = new Author();
        author.setId(1L);
        author.setName("John Doe");

        when(authorService.findbyId(1L)).thenReturn(author);

        mockMvc.perform(get("/authors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void findById_nonExistingId_throwsNotFound() throws Exception {
        when(authorService.findbyId(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/authors/{id}", 0L))
                .andExpect(status().isNotFound());
    }

    @Test
    void store_validAuthor_authorCreated() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO("Jane Doe");
        Author author = new Author("Jane Doe");

        when(authorService.store(any(AuthorDTO.class))).thenReturn(author);

        mockMvc.perform(post("/authors")
                        .content(objectMapper.writeValueAsString(authorDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    void update_existingId_authorUpdated() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO("Updated Name");
        doNothing().when(authorService).update(anyLong(), any(AuthorDTO.class));

        mockMvc.perform(put("/authors/{id}", 1L)
                        .content(objectMapper.writeValueAsString(authorDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_existingId_authorDeleted() throws Exception {
        doNothing().when(authorService).delete(anyLong());

        mockMvc.perform(delete("/authors/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
