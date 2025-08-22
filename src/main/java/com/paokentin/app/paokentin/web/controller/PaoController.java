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

    @GetMapping
    public ResponseEntity<List<Pao>> listarTodos() {
        List<Pao> paes = paoService.listarTodos();
        return ResponseEntity.ok(paes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pao> buscarPorId(@PathVariable Integer id) {
        Optional<Pao> paoOptional = paoService.buscarPorId(id);

        return paoOptional
                .map(pao -> ResponseEntity.ok(pao))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pao> cadastrarPao(@RequestBody Pao pao) {
        Pao novoPao = paoService.cadastrarPao(pao);
        return new ResponseEntity<>(novoPao, HttpStatus.CREATED);
    }
}