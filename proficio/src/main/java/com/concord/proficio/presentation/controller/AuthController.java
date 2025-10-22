package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.dto.request.AuthRequestDTO;
import com.concord.proficio.application.dto.response.AuthResponseDTO;
import com.concord.proficio.application.dto.response.UserPayloadDTO;
import com.concord.proficio.domain.entities.Colaborador;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import com.concord.proficio.infra.security.JwtTokenService;
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

@RestController
@RequestMapping("api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Colaborador principal = (Colaborador) authentication.getPrincipal();
        String token = jwtTokenService.generateToken(principal);
        UserPayloadDTO payload = new UserPayloadDTO(
                String.valueOf(principal.getId()),
                (principal.getNome() + " " + principal.getSobrenome()).trim(),
                principal.getEmail(),
                principal.getRole().getRole()
        );
        return ResponseEntity.ok(new AuthResponseDTO(token, payload));
    }
}
