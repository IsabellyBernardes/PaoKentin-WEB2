package com.paokentin.app.paokentin.domain.model;

import java.time.LocalDateTime;

public class Fornada {

    private Integer id;
    private Pao pao; // Relação com a entidade Pao
    private LocalDateTime dataHoraInicio;

    public Fornada() {
    }

    public Fornada(Integer id, Pao pao, LocalDateTime dataHoraInicio) {
        this.id = id;
        this.pao = pao;
        this.dataHoraInicio = dataHoraInicio;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pao getPao() {
        return pao;
    }

    public void setPao(Pao pao) {
        this.pao = pao;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }
}