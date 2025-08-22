package com.paokentin.app.paokentin.web.controller;

import com.paokentin.app.paokentin.domain.model.Fornada;
import com.paokentin.app.paokentin.service.FornadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/padeiro")
public class PadeiroController {

    private final FornadaService fornadaService;

    @Autowired
    public PadeiroController(FornadaService fornadaService) {
        this.fornadaService = fornadaService;
    }

    /**
     * Endpoint para o padeiro registrar uma nova fornada.
     * Mapeado para POST /api/padeiro/fornada/{paoId}
     * @param paoId O ID do pão que está entrando no forno.
     */
    @PostMapping("/fornada/{paoId}")
    public ResponseEntity<Fornada> iniciarFornada(@PathVariable Integer paoId) {
        try {
            Fornada novaFornada = fornadaService.registrarInicioFornada(paoId);
            return new ResponseEntity<>(novaFornada, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Se o serviço lançar a exceção (pão não encontrado), retornamos 404.
            return ResponseEntity.notFound().build();
        }
    }
}