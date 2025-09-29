package com.concord.proficio.presentation.controller;


import com.concord.proficio.application.service.ColaboradorService;
import com.concord.proficio.presentation.dto.ColaboradorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorResponseDTO> buscarColaborador(@PathVariable Long id) {
        return colaboradorService.buscarDetalhadoComCompetencias(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
