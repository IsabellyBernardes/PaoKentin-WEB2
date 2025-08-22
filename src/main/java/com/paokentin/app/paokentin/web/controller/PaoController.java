package com.paokentin.app.paokentin.web.controller;

import com.paokentin.app.paokentin.domain.model.Pao;
import com.paokentin.app.paokentin.service.PaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/paes")
public class PaoController {

    private final PaoService paoService;

    @Autowired
    public PaoController(PaoService paoService) {
        this.paoService = paoService;
    }

    /**
     * Endpoint para listar todos os pães.
     * Mapeado para GET /api/paes
     */
    @GetMapping
    public ResponseEntity<List<Pao>> listarTodos() {
        List<Pao> paes = paoService.listarTodos();
        return ResponseEntity.ok(paes); // Retorna 200 OK com a lista de pães
    }

    /**
     * Endpoint para buscar um pão por ID (detalhe).
     * Mapeado para GET /api/paes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pao> buscarPorId(@PathVariable Integer id) {
        Optional<Pao> paoOptional = paoService.buscarPorId(id);

        // Se o pão foi encontrado, retorna 200 OK com o pão.
        // Se não, retorna 404 Not Found.
        return paoOptional
                .map(pao -> ResponseEntity.ok(pao))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para cadastrar um novo pão.
     * Mapeado para POST /api/paes
     */
    @PostMapping
    public ResponseEntity<Pao> cadastrarPao(@RequestBody Pao pao) {
        Pao novoPao = paoService.cadastrarPao(pao);
        // Retorna 201 Created com o pão recém-criado no corpo da resposta
        return new ResponseEntity<>(novoPao, HttpStatus.CREATED);
    }
}