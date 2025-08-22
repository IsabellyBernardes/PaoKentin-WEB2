package com.paokentin.app.paokentin.service;

import com.paokentin.app.paokentin.domain.dto.FornadaStatusDTO;
import java.util.List;

import com.paokentin.app.paokentin.domain.model.Fornada;

public interface FornadaService {

    Fornada registrarInicioFornada(Integer paoId);

    List<FornadaStatusDTO> consultarStatusDasFornadas();

}