package br.com.fiap.dinfintech.model;

import java.time.LocalDate;

public class Despesa {
    private int idDespesa;
    private int idUsuario;
    private String descricao;
    private double valor;
    private LocalDate dataDespesa;
    private Categoria categoria; // MUDAR: De String para Categoria

    public Despesa() {
    }

    // Construtor para cadastro (recebe o id da categoria)
    public Despesa(int idUsuario, String descricao, double valor, LocalDate dataDespesa, Categoria categoria) {
        this.idUsuario = idUsuario;
        this.descricao = descricao;
        this.valor = valor;
        this.dataDespesa = dataDespesa;
        this.categoria = categoria;
    }

    // Construtor para recuperação do banco de dados
    public Despesa(int idDespesa, int idUsuario, String descricao, double valor, LocalDate dataDespesa, Categoria categoria) {
        this.idDespesa = idDespesa;
        this.idUsuario = idUsuario;
        this.descricao = descricao;
        this.valor = valor;
        this.dataDespesa = dataDespesa;
        this.categoria = categoria;
    }

    // Getters e Setters (Certifique-se de ter getters e setters para a Categoria)
    public int getIdDespesa() { return idDespesa; }
    public void setIdDespesa(int idDespesa) { this.idDespesa = idDespesa; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public LocalDate getDataDespesa() { return dataDespesa; }
    public void setDataDespesa(LocalDate dataDespesa) { this.dataDespesa = dataDespesa; }
    public Categoria getCategoria() { return categoria; } // NOVO GETTER
    public void setCategoria(Categoria categoria) { this.categoria = categoria; } // NOVO SETTER
}