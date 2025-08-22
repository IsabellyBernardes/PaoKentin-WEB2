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