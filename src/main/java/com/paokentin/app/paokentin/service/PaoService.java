package com.paokentin.app.paokentin.service;

import com.paokentin.app.paokentin.domain.model.Pao;

import java.util.List;
import java.util.Optional;

public interface PaoService {

    /**
     * Cadastra um novo tipo de pão.
     * @param pao O pão a ser cadastrado.
     * @return O pão cadastrado com seu ID.
     */
    Pao cadastrarPao(Pao pao);

    /**
     * Busca todos os pães cadastrados.
     * @return Uma lista de todos os pães.
     */
    List<Pao> listarTodos();

    /**
     * Busca um pão específico pelo seu ID.
     * @param id O ID do pão.
     * @return Um Optional contendo o pão, se encontrado.
     */
    Optional<Pao> buscarPorId(Integer id);
}