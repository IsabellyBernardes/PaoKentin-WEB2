package com.paokentin.app.paokentin.repository;

import com.paokentin.app.paokentin.domain.model.Fornada;

import java.util.List;

public interface FornadaRepository {

    Fornada save(Fornada fornada);

    List<Fornada> findLatestForEachPao();
}