// Exemplo de como sua classe Meta deve ser (adicione 'valorAtual' se ainda não tiver)
package br.com.fiap.dinfintech.model;

import java.time.LocalDate;

public class Meta {
    private int idMeta;
    private int idUsuario;
    private String descricao;
    private double valorAlvo;
    private double valorAtual;
    private LocalDate dataCriacao;
    private LocalDate dataAlvo;
    private String statusMeta;
    private String prioridade;

    public Meta() {

    }

    public Meta(int idUsuario, String descricao, double valorAlvo, LocalDate dataAlvo, String statusMeta, String prioridade) {
        this.idUsuario = idUsuario;
        this.descricao = descricao;
        this.valorAlvo = valorAlvo;
        this.valorAtual = 0.0;
        this.dataCriacao = LocalDate.now();
        this.dataAlvo = dataAlvo;
        this.statusMeta = statusMeta;
        this.prioridade = prioridade;
    }


    public int getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(int idMeta) {
        this.idMeta = idMeta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValorAlvo() {
        return valorAlvo;
    }

    public void setValorAlvo(double valorAlvo) {
        this.valorAlvo = valorAlvo;
    }

    public double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(double valorAtual) {
        this.valorAtual = valorAtual;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataAlvo() {
        return dataAlvo;
    }

    public void setDataAlvo(LocalDate dataAlvo) {
        this.dataAlvo = dataAlvo;
    }

    public String getStatusMeta() {
        return statusMeta;
    }

    public void setStatusMeta(String statusMeta) {
        this.statusMeta = statusMeta;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }
}