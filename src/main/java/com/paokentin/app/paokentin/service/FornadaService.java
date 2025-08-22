package com.paokentin.app.paokentin.service;

import com.paokentin.app.paokentin.domain.dto.FornadaStatusDTO;
import java.util.List;

import com.paokentin.app.paokentin.domain.model.Fornada;

public interface FornadaService {

    /**
     * Registra o início de uma nova fornada para um pão específico.
     * @param paoId O ID do pão que está indo para o forno.
     * @return A nova fornada que foi criada.
     * @throws RuntimeException se o pão com o ID fornecido não for encontrado.
     */
    Fornada registrarInicioFornada(Integer paoId);

    List<FornadaStatusDTO> consultarStatusDasFornadas();

}