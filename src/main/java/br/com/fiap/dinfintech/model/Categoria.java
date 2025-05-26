package br.com.fiap.dinfintech.model;

public class Categoria {
    private int idCategoria;
    private int idUsuario;
    private String nome;
    private String tipo;
    private boolean preDefinida;

    public Categoria() {
    }

    public Categoria(int idUsuario, String nome, String tipo) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.tipo = tipo;
        this.preDefinida = false;
    }

    public Categoria(int idCategoria, int idUsuario, String nome, String tipo, boolean preDefinida) {
        this.idCategoria = idCategoria;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.tipo = tipo;
        this.preDefinida = preDefinida;
    }


    public int getIdCategoria() {
        return idCategoria;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isPreDefinida() {
        return preDefinida;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPreDefinida(boolean preDefinida) {
        this.preDefinida = preDefinida;
    }
}