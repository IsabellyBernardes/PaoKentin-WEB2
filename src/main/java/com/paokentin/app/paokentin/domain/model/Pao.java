package com.paokentin.app.paokentin.domain.model;

public class Pao {

    private Integer id;
    private String nome;
    private String descricao;
    private int tempoPreparoMinutos;
    private String corHex;

    // Construtor padrão (obrigatório para muitas bibliotecas)
    public Pao() {
    }

    // Construtor completo para facilitar a criação de objetos
    public Pao(Integer id, String nome, String descricao, int tempoPreparoMinutos) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.tempoPreparoMinutos = tempoPreparoMinutos;
        this.corHex = corHex;
    }

    // Getters e Setters para todos os atributos
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getTempoPreparoMinutos() {
        return tempoPreparoMinutos;
    }

    public void setTempoPreparoMinutos(int tempoPreparoMinutos) {
        this.tempoPreparoMinutos = tempoPreparoMinutos;
    }

    public String getCorHex() {
        return corHex;
    }

    public void setCorHex(String corHex) {
        this.corHex = corHex;
    }
}