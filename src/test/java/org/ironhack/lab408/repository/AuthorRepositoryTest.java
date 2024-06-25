package org.ironhack.lab408.repository;

import org.ironhack.lab408.model.Author;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AuthorRepositoryTest {
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
    void saveAuthor_newAuthor_authorSaved() {
        Author author = new Author();
        author.setName("Jane Doe");
        Author savedAuthor = authorRepository.save(author);
        assertNotNull(savedAuthor);
        assertEquals("Jane Doe", savedAuthor.getName());
    }
}