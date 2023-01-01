package com.agency.finalproject.configuration;

import com.agency.finalproject.entity.role.ERole;
import com.agency.finalproject.entity.role.Role;
import com.agency.finalproject.repository.role.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoleDataLoader implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleDataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (ERole eRole : ERole.values()) {
            if (!this.roleRepository.existsByName(eRole)) {
                this.roleRepository.save(new Role(eRole));
            }
        }
        log.info("Roles were populated successfully.");
    }

}
