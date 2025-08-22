package com.paokentin.app.paokentin.repository;

import com.paokentin.app.paokentin.domain.model.Pao;
import java.util.List;
import java.util.Optional;

public interface PaoRepository {

    Pao save(Pao pao);

    Optional<Pao> findById(Integer id);

    List<Pao> findAll();

}