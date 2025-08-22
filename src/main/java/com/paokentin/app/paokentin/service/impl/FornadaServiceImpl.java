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
        // 1. Busca o pão no banco de dados para garantir que ele existe.
        Pao pao = paoRepository.findById(paoId)
                .orElseThrow(() -> new RuntimeException("Pão com ID " + paoId + " não encontrado."));

        // 2. Cria um novo objeto Fornada.
        Fornada novaFornada = new Fornada();
        novaFornada.setPao(pao);
        novaFornada.setDataHoraInicio(LocalDateTime.now()); // Pega a data e hora exatas do servidor.

        // 3. Salva a nova fornada no banco.
        return fornadaRepository.save(novaFornada);
    }

    // Remova os imports do ZoneId e ZonedDateTime se não forem mais usados em outro lugar

    @Override
    @Transactional(readOnly = true)
    public List<FornadaStatusDTO> consultarStatusDasFornadas() {
        List<Fornada> ultimasFornadas = fornadaRepository.findLatestForEachPao();
        List<FornadaStatusDTO> statusList = new ArrayList<>();

        // Pega a hora atual. Simples.
        LocalDateTime agora = LocalDateTime.now();

        final int JANELA_DE_FRESCOR_EM_MINUTOS = 60;

        for (Fornada fornada : ultimasFornadas) {
            Pao pao = fornada.getPao();
            LocalDateTime horaInicio = fornada.getDataHoraInicio();
            int tempoPreparo = pao.getTempoPreparoMinutos();

            LocalDateTime horaPrevistaParaFicarPronto = horaInicio.plusMinutes(tempoPreparo);
            LocalDateTime horaDeExpiracao = horaPrevistaParaFicarPronto.plusMinutes(JANELA_DE_FRESCOR_EM_MINUTOS);

            // A lógica de expiração
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