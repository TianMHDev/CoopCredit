package com.coopcredit.application.infrastructure.adapter.in.web.controller;

import com.coopcredit.application.domain.model.Role;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.auth.JwtResponse;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.auth.LoginRequest;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.auth.MessageResponse;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.auth.RegisterRequest;
import com.coopcredit.application.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.coopcredit.application.infrastructure.adapter.out.persistence.repository.UserJpaRepository;
import com.coopcredit.application.infrastructure.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Authentication endpoints")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserJpaRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    @Operation(summary = "Authenticate user and return JWT token")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                null, // ID not easily accessible from UserDetails unless custom implementation
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Create new user's account
        UserEntity user = new UserEntity();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        // Default role: AFILIADO unless specified (simplified logic)
        Role role = Role.ROLE_AFILIADO;
        if (signUpRequest.getRole() != null && !signUpRequest.getRole().isEmpty()) {
            String roleStr = signUpRequest.getRole().iterator().next();
            try {
                role = Role.valueOf(roleStr);
            } catch (IllegalArgumentException e) {
                // ignore, default to AFILIADO or handle error
            }
        }
        user.setRole(role);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
