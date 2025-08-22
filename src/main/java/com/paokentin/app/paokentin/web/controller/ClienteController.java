package com.paokentin.app.paokentin.web.controller;

import com.paokentin.app.paokentin.domain.dto.FornadaStatusDTO;
import com.paokentin.app.paokentin.service.FornadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private final FornadaService fornadaService;

    @Autowired
    public ClienteController(FornadaService fornadaService) {
        this.fornadaService = fornadaService;
    }

    /**
     * Endpoint para o cliente consultar o status de todas as últimas fornadas.
     * Mapeado para GET /api/cliente/status-fornadas
     */
    @GetMapping("/status-fornadas")
    public ResponseEntity<List<FornadaStatusDTO>> obterStatusDasFornadas() {
        List<FornadaStatusDTO> statusList = fornadaService.consultarStatusDasFornadas();
        return ResponseEntity.ok(statusList); // Retorna 200 OK com a lista de status
    }
}