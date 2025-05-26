package br.com.fiap.dinfintech.model;

import java.time.LocalDate;

public class Receita {
    private int idReceita;
    private int idUsuario;
    private String descricao;
    private double valor;
    private LocalDate dataReceita;
    private String tipoReceita;

    public Receita(int idUsuario, String descricao, double valor, LocalDate dataReceita, String tipoReceita) {
        this.idUsuario = idUsuario;
        this.descricao = descricao;
        this.valor = valor;
        this.dataReceita = dataReceita;
        this.tipoReceita = tipoReceita;
    }

    public Receita(int idReceita, int idUsuario, String descricao, double valor, LocalDate dataReceita, String tipoReceita) {
        this.idReceita = idReceita;
        this.idUsuario = idUsuario;
        this.descricao = descricao;
        this.valor = valor;
        this.dataReceita = dataReceita;
        this.tipoReceita = tipoReceita;
    }

    public int getIdReceita() {
        return idReceita;
    }

    public void setIdReceita(int idReceita) {
        this.idReceita = idReceita;
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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataReceita() {
        return dataReceita;
    }

    public void setDataReceita(LocalDate dataReceita) {
        this.dataReceita = dataReceita;
    }

    public String getTipoReceita() {
        return tipoReceita;
    }

    public void setTipoReceita(String tipoReceita) {
        this.tipoReceita = tipoReceita;
    }

    @Override
    public String toString() {
        return "Receita{" +
            "idReceita=" + idReceita +
            ", idUsuario=" + idUsuario +
            ", descricao='" + descricao + '\'' +
            ", valor=" + valor +
            ", dataReceita=" + dataReceita +
            ", tipoReceita='" + tipoReceita + '\'' +
            '}';
    }
}