package com.paokentin.app.paokentin.service.impl;

import com.paokentin.app.paokentin.domain.model.Pao;
import com.paokentin.app.paokentin.repository.PaoRepository;
import com.paokentin.app.paokentin.service.PaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PaoServiceImpl implements PaoService {

    private final PaoRepository paoRepository;

    @Autowired
    public PaoServiceImpl(PaoRepository paoRepository) {
        this.paoRepository = paoRepository;
    }

    @Override
    @Transactional
    public Pao cadastrarPao(Pao pao) {
        return paoRepository.save(pao);
    }

    @Override
    @Transactional
    public Pao atualizarPao(Integer id, Pao paoAtualizado) {
        // Primeiro, garante que o pão existe antes de tentar atualizá-lo.
        Pao paoExistente = paoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pão com ID " + id + " não encontrado."));

        // Atualiza os dados do objeto existente com os novos dados
        paoExistente.setNome(paoAtualizado.getNome());
        paoExistente.setDescricao(paoAtualizado.getDescricao());
        paoExistente.setTempoPreparoMinutos(paoAtualizado.getTempoPreparoMinutos());
        paoExistente.setCorHex(paoAtualizado.getCorHex());

        // Salva o objeto atualizado no banco
        return paoRepository.update(paoExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pao> listarTodos() {
        return paoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pao> buscarPorId(Integer id) {
        return paoRepository.findById(id);
    }
}