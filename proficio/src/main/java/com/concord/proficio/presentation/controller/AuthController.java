package com.concord.proficio.presentation.controller;

import com.concord.proficio.infra.security.JwtTokenService;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import com.concord.proficio.domain.entities.Colaborador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("api")
public class AuthController {

    public static record LoginRequest(@Email String email, @NotBlank String password) {}
    public static record LoginResponse(String token, UserPayload user) {}
    public static record UserPayload(String id, String name, String email, String role) {}

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Colaborador principal = (Colaborador) authentication.getPrincipal();
        String token = jwtTokenService.generateToken(principal);
        UserPayload payload = new UserPayload(
                String.valueOf(principal.getId()),
                (principal.getNome() + " " + principal.getSobrenome()).trim(),
                principal.getEmail(),
                principal.getRole().getRole()
        );
        return ResponseEntity.ok(new LoginResponse(token, payload));
    }
}
