package org.ironhack.lab408.repository;

import org.ironhack.lab408.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByName_existingName_roleReturned() {
        Role role1 = new Role(null, "ROLE_USER");
        Role role2 = new Role(null, "ROLE_ADMIN");

        roleRepository.save(role1);
        roleRepository.save(role2);

        Role found = roleRepository.findByName("ROLE_USER");
        assertNotNull(found);
        assertEquals("ROLE_USER", found.getName());

        roleRepository.delete(role1);
        roleRepository.delete(role2);
    }
}
