package com.paokentin.app.paokentin.service;

import com.paokentin.app.paokentin.domain.model.Pao;

import java.util.List;
import java.util.Optional;

public interface PaoService {

    Pao cadastrarPao(Pao pao);
    Pao atualizarPao(Integer id, Pao paoAtualizado);
    List<Pao> listarTodos();

    Optional<Pao> buscarPorId(Integer id);
}