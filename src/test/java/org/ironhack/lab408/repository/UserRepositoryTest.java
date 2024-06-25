package org.ironhack.lab408.repository;

import org.ironhack.lab408.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setUsername("testUser1");
        User user2 = new User();
        user2.setUsername("testUser2");
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findByUsername_existingUsername_userReturned() {
        User found = userRepository.findByUsername("testUser1");
        assertNotNull(found);
        assertEquals("testUser1", found.getUsername());
    }

    @Test
    void existsByUsername_existingUsername_trueReturned() {
        Boolean exists = userRepository.existsByUsername("testUser1");
        assertTrue(exists);
    }

    @Test
    void existsByUsername_nonExistingUsername_falseReturned() {
        Boolean exists = userRepository.existsByUsername("nonExistingUser");
        assertFalse(exists);
    }
}