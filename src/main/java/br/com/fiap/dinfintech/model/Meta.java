package br.com.fiap.dinfintech.model;

import java.time.LocalDate;

public class Meta {
    private int idMeta;
    private int idUsuario;
    private String descricao;
    private double valorAlvo;
    private double valorAtual; // Novo campo
    private LocalDate dataCriacao; // Novo campo
    private LocalDate dataAlvo;
    private String statusMeta; // Renomeado de 'status' para 'statusMeta'
    private String prioridade; // Novo campo

    public Meta() {
    }

    // Constructor for creating new Meta (from form, without idMeta or dataCriacao initially)
    public Meta(int idUsuario, String descricao, double valorAlvo, LocalDate dataAlvo, String statusMeta, String prioridade) {
        this.idUsuario = idUsuario;
        this.descricao = descricao;
        this.valorAlvo = valorAlvo;
        this.valorAtual = 0.0; // Valor inicial para nova meta, será DEFAULT 0 no banco
        this.dataCriacao = LocalDate.now(); // Define a data de criação como a data atual
        this.dataAlvo = dataAlvo;
        this.statusMeta = statusMeta;
        this.prioridade = prioridade;
    }

    // Constructor for retrieving Meta from DB (with all fields)
    public Meta(int idMeta, int idUsuario, String descricao, double valorAlvo, double valorAtual, LocalDate dataCriacao, LocalDate dataAlvo, String statusMeta, String prioridade) {
        this.idMeta = idMeta;
        this.idUsuario = idUsuario;
        this.descricao = descricao;
        this.valorAlvo = valorAlvo;
        this.valorAtual = valorAtual;
        this.dataCriacao = dataCriacao;
        this.dataAlvo = dataAlvo;
        this.statusMeta = statusMeta;
        this.prioridade = prioridade;
    }

    // Getters
    public int getIdMeta() {
        return idMeta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValorAlvo() {
        return valorAlvo;
    }

    public double getValorAtual() {
        return valorAtual;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public LocalDate getDataAlvo() {
        return dataAlvo;
    }

    public String getStatusMeta() {
        return statusMeta;
    }

    public String getPrioridade() {
        return prioridade;
    }

    // Setters
    public void setIdMeta(int idMeta) {
        this.idMeta = idMeta;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValorAlvo(double valorAlvo) {
        this.valorAlvo = valorAlvo;
    }

    public void setValorAtual(double valorAtual) {
        this.valorAtual = valorAtual;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataAlvo(LocalDate dataAlvo) {
        this.dataAlvo = dataAlvo;
    }

    public void setStatusMeta(String statusMeta) {
        this.statusMeta = statusMeta;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }
}