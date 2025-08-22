package com.paokentin.app.paokentin.domain.dto;



import java.time.LocalDateTime;

public class FornadaStatusDTO {

    private String nomePao;
    private String descricaoPao;

    private String status; // Ex: "Assando" ou "Pronto"
    private LocalDateTime horaPrevistaParaFicarPronto;

    private long tempoRestanteSegundos;

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