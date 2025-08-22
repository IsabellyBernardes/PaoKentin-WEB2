package com.paokentin.app.paokentin.repository;

import com.paokentin.app.paokentin.domain.model.Pao;
import java.util.List;
import java.util.Optional;

public interface PaoRepository {

    /**
     * Salva um novo pão no banco de dados.
     * @param pao O objeto Pao a ser salvo, sem o ID.
     * @return O objeto Pao salvo, agora com o ID atribuído pelo banco.
     */
    Pao save(Pao pao);

    /**
     * Busca um pão pelo seu ID.
     * @param id O ID do pão a ser encontrado.
     * @return um Optional contendo o Pao se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<Pao> findById(Integer id);

    /**
     * Retorna uma lista com todos os pães cadastrados no banco de dados.
     * @return Uma lista de objetos Pao.
     */
    List<Pao> findAll();

}