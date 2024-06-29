package org.ironhack.lab408.service;

import org.ironhack.lab408.dtos.AuthorDTO;
import org.ironhack.lab408.model.Author;
import org.ironhack.lab408.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
class AuthorServiceUnitTest {

    // Creates an instance of the class and injects the mocks into it.
    @InjectMocks
    private AuthorService authorService;

    // Creates an instance of the mock
    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        // Initializes mocks annotated with @Mock and injects these mocks into the fields annotated with @InjectMocks.
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_existingId_authorReturned() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author(1L, "John Doe", null)));

        Author found = authorService.findbyId(1L);
        assertNotNull(found);
        assertEquals("John Doe", found.getName());
    }

    @Test
    void findById_nonExistingId_throwsNotFound() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> authorService.findbyId(1L));
    }

    @Test
    void store_validAuthor_authorSaved() {
        AuthorDTO authorDTO = new AuthorDTO("Jane Doe");
        Author author = new Author("Jane Doe");
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Author saved = authorService.store(authorDTO);
        assertNotNull(saved);
        assertEquals("Jane Doe", saved.getName());
    }

    @Test
    void update_existingId_authorUpdated() {
        Author author = new Author(1L, "John Doe", null);
        AuthorDTO authorDTO = new AuthorDTO("Updated Name");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        authorService.update(1L, authorDTO);
        assertEquals("Updated Name", author.getName());
    }

    @Test
    void update_nonExistingId_throwsNotFound() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());
        AuthorDTO authorDTO = new AuthorDTO("Updated Name");
        assertThrows(ResponseStatusException.class, () -> authorService.update(1L, authorDTO));
    }

    @Test
    void delete_existingId_authorDeleted() {
        Author author = new Author();
        author.setId(1L);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        doNothing().when(authorRepository).delete(any(Author.class));

        authorService.delete(1L);
        verify(authorRepository, times(1)).delete(author);
    }

    @Test
    void delete_nonExistingId_throwsNotFound() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> authorService.delete(1L));
    }
}
