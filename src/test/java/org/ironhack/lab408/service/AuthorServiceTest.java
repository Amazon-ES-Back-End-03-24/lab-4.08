package org.ironhack.lab408.service;

import org.ironhack.lab408.dtos.AuthorDTO;
import org.ironhack.lab408.model.Author;
import org.ironhack.lab408.repository.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        Author author = new Author();
        author.setName("John Doe");
        authorRepository.save(author);
    }

    @AfterEach
    void tearDown() {
        authorRepository.deleteAll();
    }

    @Test
    void findById_existingId_authorReturned() {
        Author author = authorRepository.findAll().get(0);
        Author found = authorService.findbyId(author.getId());
        assertNotNull(found);
        assertEquals("John Doe", found.getName());
    }

    @Test
    void findById_nonExistingId_throwsNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            authorService.findbyId(0L);
        });
    }

    @Test
    void store_validAuthor_authorSaved() {
        AuthorDTO authorDTO = new AuthorDTO("Jane Doe");
        Author saved = authorService.store(authorDTO);
        assertNotNull(saved);
        assertEquals("Jane Doe", saved.getName());
    }

    @Test
    void update_existingId_authorUpdated() {
        Author author = authorRepository.findAll().get(0);
        AuthorDTO authorDTO = new AuthorDTO("Updated Name");
        authorService.update(author.getId(), authorDTO);
        Author updated = authorRepository.findById(author.getId()).get();
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void update_nonExistingId_throwsNotFound() {
        AuthorDTO authorDTO = new AuthorDTO("Updated Name");
        assertThrows(ResponseStatusException.class, () -> {
            authorService.update(0L, authorDTO);
        });
    }

    @Test
    void delete_existingId_authorDeleted() {
        Author author = authorRepository.findAll().get(0);
        authorService.delete(author.getId());
        assertFalse(authorRepository.findById(author.getId()).isPresent());
    }

    @Test
    void delete_nonExistingId_throwsNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            authorService.delete(0L);
        });
    }

    @Test
    void findById_existingId_returnsAuthor() {
        Author author = authorRepository.findAll().get(0);
        Author found = authorService.findbyId(author.getId());
        assertNotNull(found);
        assertEquals("John Doe", found.getName());
    }

    @Test
    void update_existingId_updatesAuthor() {
        Author author = authorRepository.findAll().get(0);
        AuthorDTO authorDTO = new AuthorDTO("Updated Name");
        authorService.update(author.getId(), authorDTO);
        Author updated = authorRepository.findById(author.getId()).get();
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void delete_existingId_deletesAuthor() {
        Author author = authorRepository.findAll().get(0);
        authorService.delete(author.getId());
        assertFalse(authorRepository.findById(author.getId()).isPresent());
    }
}