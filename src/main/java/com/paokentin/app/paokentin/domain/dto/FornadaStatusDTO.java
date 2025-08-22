package com.paokentin.app.paokentin.domain.dto;



import java.time.LocalDateTime;

public class FornadaStatusDTO {

    // Informações do Pão
    private String nomePao;
    private String descricaoPao;

    // Status da Fornada
    private String status; // Ex: "Assando" ou "Pronto"
    private LocalDateTime horaPrevistaParaFicarPronto;

    // O contador para o front-end
    private long tempoRestanteSegundos;

    // Getters e Setters
    public String getNomePao() {
        return nomePao;
    }

    public void setNomePao(String nomePao) {
        this.nomePao = nomePao;
    }

    public String getDescricaoPao() {
        return descricaoPao;
    }

    public void setDescricaoPao(String descricaoPao) {
        this.descricaoPao = descricaoPao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getHoraPrevistaParaFicarPronto() {
        return horaPrevistaParaFicarPronto;
    }

    public void setHoraPrevistaParaFicarPronto(LocalDateTime horaPrevistaParaFicarPronto) {
        this.horaPrevistaParaFicarPronto = horaPrevistaParaFicarPronto;
    }

    public long getTempoRestanteSegundos() {
        return tempoRestanteSegundos;
    }

    public void setTempoRestanteSegundos(long tempoRestanteSegundos) {
        this.tempoRestanteSegundos = tempoRestanteSegundos;
    }
}