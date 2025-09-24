package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.dto.request.AuthRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthRequestDTO dto) {
        var data = new UsernamePasswordAuthenticationToken(dto.email, dto.password);
        var auth = this.authenticationManager.authenticate(data);

        return ResponseEntity.ok().build();
    }
}
