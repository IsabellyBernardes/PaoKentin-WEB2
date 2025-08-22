package com.paokentin.app.paokentin.repository;

import com.paokentin.app.paokentin.domain.model.Fornada;

import java.util.List;

public interface FornadaRepository {

    /**
     * Salva uma nova fornada no banco de dados.
     * @param fornada A fornada a ser salva.
     * @return A fornada salva com o ID atribuído.
     */
    Fornada save(Fornada fornada);

    /**
     * Busca a fornada mais recente para cada tipo de pão cadastrado.
     * Essencial para a tela do cliente.
     * @return Uma lista com as últimas fornadas de cada pão.
     */
    List<Fornada> findLatestForEachPao();
}