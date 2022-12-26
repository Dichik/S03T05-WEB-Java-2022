package com.agency.finalproject.configuration;

import com.agency.finalproject.entity.role.ERole;
import com.agency.finalproject.entity.role.Role;
import com.agency.finalproject.repository.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RolesGenerationLoader implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public RolesGenerationLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (ERole eRole : ERole.values()) {
            if (!this.roleRepository.existsByName(eRole)) {
                this.roleRepository.save(new Role(eRole));
            }
        }
    }

}
