package com.paokentin.app.paokentin.service.impl;

import com.paokentin.app.paokentin.domain.dto.FornadaStatusDTO;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.paokentin.app.paokentin.domain.model.Fornada;
import com.paokentin.app.paokentin.domain.model.Pao;
import com.paokentin.app.paokentin.repository.FornadaRepository;
import com.paokentin.app.paokentin.repository.PaoRepository;
import com.paokentin.app.paokentin.service.FornadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FornadaServiceImpl implements FornadaService {

    private final FornadaRepository fornadaRepository;
    private final PaoRepository paoRepository;

    @Autowired
    public FornadaServiceImpl(FornadaRepository fornadaRepository, PaoRepository paoRepository) {
        this.fornadaRepository = fornadaRepository;
        this.paoRepository = paoRepository;
    }

    @Override
    @Transactional
    public Fornada registrarInicioFornada(Integer paoId) {
        Pao pao = paoRepository.findById(paoId)
                .orElseThrow(() -> new RuntimeException("Pão com ID " + paoId + " não encontrado."));

        Fornada novaFornada = new Fornada();
        novaFornada.setPao(pao);
        novaFornada.setDataHoraInicio(LocalDateTime.now()); // Pega a data e hora exatas do servidor.

        return fornadaRepository.save(novaFornada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FornadaStatusDTO> consultarStatusDasFornadas() {
        List<Fornada> ultimasFornadas = fornadaRepository.findLatestForEachPao();
        List<FornadaStatusDTO> statusList = new ArrayList<>();

        LocalDateTime agora = LocalDateTime.now();

        final int JANELA_DE_FRESCOR_EM_MINUTOS = 60;

        for (Fornada fornada : ultimasFornadas) {
            Pao pao = fornada.getPao();
            LocalDateTime horaInicio = fornada.getDataHoraInicio();
            int tempoPreparo = pao.getTempoPreparoMinutos();

            LocalDateTime horaPrevistaParaFicarPronto = horaInicio.plusMinutes(tempoPreparo);
            LocalDateTime horaDeExpiracao = horaPrevistaParaFicarPronto.plusMinutes(JANELA_DE_FRESCOR_EM_MINUTOS);

            if (agora.isAfter(horaDeExpiracao)) {
                continue;
            }

            FornadaStatusDTO dto = new FornadaStatusDTO();
            dto.setNomePao(pao.getNome());
            dto.setDescricaoPao(pao.getDescricao());
            dto.setHoraPrevistaParaFicarPronto(horaPrevistaParaFicarPronto);

            if (agora.isBefore(horaPrevistaParaFicarPronto)) {
                dto.setStatus("Assando");
                Duration duracaoRestante = Duration.between(agora, horaPrevistaParaFicarPronto);
                dto.setTempoRestanteSegundos(duracaoRestante.toSeconds());
            } else {
                dto.setStatus("Pronto");
                dto.setTempoRestanteSegundos(0);
            }

            statusList.add(dto);
        }

        return statusList;
    }

}