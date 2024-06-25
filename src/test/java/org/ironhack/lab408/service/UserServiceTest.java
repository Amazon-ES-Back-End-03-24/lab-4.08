package org.ironhack.lab408.service;

import org.ironhack.lab408.model.Role;
import org.ironhack.lab408.model.User;
import org.ironhack.lab408.repository.RoleRepository;
import org.ironhack.lab408.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setName("Test User");
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void loadUserByUsername_existingUsername_userDetailsReturned() {
        UserDetails userDetails = userService.loadUserByUsername("testUser");
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_nonExistingUsername_throwsUsernameNotFound() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonExistingUser");
        });
    }

    @Test
    void saveUser_validUser_userSaved() {
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("newPassword");
        user.setName("New User");

        User saved = userService.saveUser(user);
        assertNotNull(saved);
        assertEquals("newUser", saved.getUsername());
    }

    @Test
    void saveUser_existingUsername_throwsConflict() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("newPassword");
        user.setName("New User");

        assertThrows(ResponseStatusException.class, () -> {
            userService.saveUser(user);
        });
    }

    @Test
    void getUser_existingUsername_userReturned() {
        User user = userService.getUser("testUser");
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
    }

    @Test
    void getUser_nonExistingUsername_userReturned() {
        User user = userService.getUser("nonExistingUser");
        assertNull(user);
    }

    @Test
    void getUsers_allUsers_usersReturned() {
        List<User> users = userService.getUsers();
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }

    @Test
    void saveRole_validRole_roleSaved() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        Role saved = userService.saveRole(role);
        assertNotNull(saved);
        assertEquals("ROLE_ADMIN", saved.getName());
    }

    @Test
    void addRoleToUser_validRole_roleAdded() {
        userService.addRoleToUser("testUser", "ROLE_USER");
        User user = userRepository.findByUsername("testUser");
        assertTrue(user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_USER")));
    }
}