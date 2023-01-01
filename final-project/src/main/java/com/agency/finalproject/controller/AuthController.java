package com.agency.finalproject.controller;

import com.agency.finalproject.entity.login.request.LoginRequest;
import com.agency.finalproject.entity.login.request.SignupRequest;
import com.agency.finalproject.entity.login.response.JwtResponse;
import com.agency.finalproject.entity.login.response.MessageResponse;
import com.agency.finalproject.entity.role.ERole;
import com.agency.finalproject.entity.role.Role;
import com.agency.finalproject.entity.user.User;
import com.agency.finalproject.repository.role.RoleRepository;
import com.agency.finalproject.repository.user.UserRepository;
import com.agency.finalproject.security.jwt.JwtUtils;
import com.agency.finalproject.security.service.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new JwtResponse(jwt,
                userDetails.id(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles), HttpStatus.OK);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (this.userRepository.existsByUsername(signUpRequest.getUsername())) {
            log.warn(String.format("Can't signup as username=[%s] is already taken.", signUpRequest.getUsername()));
            return new ResponseEntity<>(new MessageResponse("Error: Username is already taken!"), HttpStatus.BAD_REQUEST);
        }

        if (this.userRepository.existsByEmail(signUpRequest.getEmail())) {
            log.warn(String.format("Can't signup as email=[%s] is already taken.", signUpRequest.getEmail()));
            return new ResponseEntity<>(new MessageResponse("Error: Email is already in use!"), HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(this.encoder.encode(signUpRequest.getPassword()))
                .build();

        Set<String> requestRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (requestRoles == null) {
            Role userRole = this.roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new EntityNotFoundException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            handleRoles(requestRoles, roles);
        }

        user.setRoles(roles);
        this.userRepository.save(user);

        return new ResponseEntity<>(new MessageResponse("User registered successfully!"), HttpStatus.OK);
    }

    private void handleRoles(Set<String> requestRoles, Set<Role> roles) {
        requestRoles.forEach(role -> {
            switch (role.toUpperCase()) {
                case "MANAGER" -> {
                    Role managerRole = this.roleRepository.findByName(ERole.ROLE_MANAGER)
                            .orElseThrow(() -> new EntityNotFoundException("Error: Role is not found."));
                    roles.add(managerRole);
                }
                case "MASTER" -> {
                    Role masterRole = this.roleRepository.findByName(ERole.ROLE_MASTER)
                            .orElseThrow(() -> new EntityNotFoundException("Error: Role is not found."));
                    roles.add(masterRole);
                }
                default -> {
                    Role userRole = this.roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new EntityNotFoundException("Error: Role is not found."));
                    roles.add(userRole);
                }
            }
        });
    }

    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = this.jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

}
