// br.com.fiap.dinfintech.model.Usuario.java

package br.com.fiap.dinfintech.model;

public class Usuario {
    private int idUsuario;
    private String nome;
    private String sobrenome;
    private String dtNascimento;
    private String cpf;
    private String sexo;
    private String email;
    private String senha;
    private double saldo; // NOVO: Atributo para armazenar o saldo

    public Usuario() {
    }

    // Construtor para cadastro (sem ID e sem saldo inicial)
    public Usuario(String nome, String sobrenome, String dtNascimento, String cpf, String sexo, String email, String senha) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dtNascimento = dtNascimento;
        this.cpf = cpf;
        this.sexo = sexo;
        this.email = email;
        this.senha = senha;
        this.saldo = 0.0; // Saldo inicial pode ser 0
    }

    // Construtor completo (útil para recuperar do banco) - ADICIONE O SALDO AQUI
    public Usuario(int idUsuario, String nome, String sobrenome, String dtNascimento, String cpf, String sexo, String email, String senha, double saldo) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dtNascimento = dtNascimento;
        this.cpf = cpf;
        this.sexo = sexo;
        this.email = email;
        this.senha = senha;
        this.saldo = saldo;
    }

    public Usuario(int idUsuario, String nome, String email, String senha) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.saldo = 0.0; // Assume saldo inicial 0, já que não é buscado nesta query
        // Outros campos (sobrenome, dtNascimento, cpf, sexo) serão nulos/padrão,
        // pois não são fornecidos por este construtor.
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(String dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}