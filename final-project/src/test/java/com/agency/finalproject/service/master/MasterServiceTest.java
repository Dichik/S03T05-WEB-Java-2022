package com.agency.finalproject.service.master;

import com.agency.finalproject.entity.role.ERole;
import com.agency.finalproject.entity.role.Role;
import com.agency.finalproject.entity.user.User;
import com.agency.finalproject.repository.role.RoleRepository;
import com.agency.finalproject.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class MasterServiceTest {

    private static final String DEFAULT_USERNAME = "master";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MasterService masterService;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Role role = this.roleRepository.findByName(ERole.ROLE_MASTER)
                .orElseThrow(() -> new EntityNotFoundException("Role was not found."));
        Set<Role> roles = Set.of(role);
        User MASTER = this.userRepository.save(User.builder()
                .username(DEFAULT_USERNAME)
                .password("password")
                .email("master@user.com")
                .roles(roles)
                .build());
        assertNotNull(MASTER, "Couldn't save master");
    }

    @Test
    void findAllMasters() {
        List<User> masters = this.masterService.findAllMasters();
        assertEquals(masters.size(), 1, "Number of masters must be 1.");
        assertEquals(masters.get(0).getUsername(), DEFAULT_USERNAME);
    }

}