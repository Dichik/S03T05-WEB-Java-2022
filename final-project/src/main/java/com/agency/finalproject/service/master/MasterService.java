package com.agency.finalproject.service.master;

import com.agency.finalproject.entity.role.ERole;
import com.agency.finalproject.entity.role.Role;
import com.agency.finalproject.entity.user.User;
import com.agency.finalproject.repository.role.RoleRepository;
import com.agency.finalproject.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class MasterService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public MasterService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> findAllMasters() {
        Role role = this.roleRepository.findByName(ERole.ROLE_MASTER)
                .orElseThrow(() -> new EntityNotFoundException("Role was not found."));
        return this.userRepository.findAllByRolesContaining(role);
    }

}
